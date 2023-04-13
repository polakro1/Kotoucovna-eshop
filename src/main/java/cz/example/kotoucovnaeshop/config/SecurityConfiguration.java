package cz.example.kotoucovnaeshop.config;

import cz.example.kotoucovnaeshop.service.AdminDetailsServiceImpl;
import cz.example.kotoucovnaeshop.service.UserDetailsServiceImpl;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                        .defaultSuccessUrl("/ucet", true)
                        .permitAll()
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


