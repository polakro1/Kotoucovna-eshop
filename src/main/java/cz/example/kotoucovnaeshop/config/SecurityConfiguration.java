package cz.example.kotoucovnaeshop.config;

import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.service.AdminDetailsServiceImpl;
import cz.example.kotoucovnaeshop.service.ShoppingCartService;
import cz.example.kotoucovnaeshop.service.CustomerDetailsService;
import cz.example.kotoucovnaeshop.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfiguration {
    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;
    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private CustomerAuthenticationSuccesHandler customerLoginHandler;
    @Autowired
    private AdminAuthenticationSuccesHandler adminLoginHandler;
    @Autowired
    LogoutSuccessHandler logoutHandler;
    private static final String[] PUBLIC_MATCHERS = {
            "/",
            "/css/**",
            "/fonts/**",
            "/images/**",
            "/login-user",
            "/*/produkty",
            "/*/produkt/**",
            "/cart/**",
            "/admin/login",
            "/registration",
            "/search/**",
            "/password-reset",
            "/logout-admin"

    };

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainAdmin(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(adminAuthenticationProvider())
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAuthority("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .permitAll()
                        .successHandler(adminLoginHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(logoutHandler)
                )
        ;
        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http
                .authenticationProvider(userAuthenticationProvider())
                .csrf().disable()
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
                        .requestMatchers("/ucet").hasAuthority("USER")
                         )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(customerLoginHandler)
                        )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(logoutHandler)
                        );
        return http.build();
    }

    @Bean
    public   DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(adminDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


    @Bean
    public   DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customerDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
@Component
class CustomerAuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        Client client = customerService.getByUsername(authentication.getName());
        cartService.setCartByClient(client);
        response.sendRedirect("/ucet");
    }

}

@Component
class AdminAuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        response.sendRedirect("/admin/dashboard");
    }
}

@Component
class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            response.sendRedirect("/admin/login");
        } else if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            response.sendRedirect("/login");
        }

    }
}



