package cz.example.kotoucovnaeshop.config;

import cz.example.kotoucovnaeshop.model.Client;
import cz.example.kotoucovnaeshop.service.AdminDetailsServiceImpl;
import cz.example.kotoucovnaeshop.service.ShoppingCartService;
import cz.example.kotoucovnaeshop.service.UserDetailsServiceImpl;
import cz.example.kotoucovnaeshop.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfiguration {
    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationSuccesHandler userLoginHandler;
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
            "/search/**"
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
                        .defaultSuccessUrl("/admin/dashboard")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/admin/login")
                )
               // .addFilter(adminAuthenticationFilter())
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
                        .loginPage("/login-user")
                        .permitAll()
                        .successHandler(userLoginHandler)
                        )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login-user")
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
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
@Component
class AuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        System.out.println("gg");
        Client client = userService.getByUsername(authentication.getName());
        cartService.setCartByClient(client);
        response.sendRedirect("/ucet");

        super.onAuthenticationSuccess(request, response, authentication);
    }

}


