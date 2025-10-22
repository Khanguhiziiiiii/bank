package org.khanguhizi.bankmanagementsystem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.khanguhizi.bankmanagementsystem.service.CustomUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                   /*
                   CSRF-Cross-Site Request Forgery. It’s a security attack where a malicious site tricks a
                       logged-in user into performing unwanted actions on a different site where they're already
                       authenticated.
                   How Spring Security Handles CSRF
                    By default, Spring Security enables CSRF protection for stateful web applications —
                    especially for forms or sessions in browser-based apps. It does this by:
                            Generating a CSRF token for each session
                            Requiring that token in any modifying requests (POST, PUT, DELETE, etc.)
                            If the CSRF token is missing or incorrect, Spring blocks the request.
                   In this case, CSRF is not necessary — because:
                        No session is maintained (you’re using Basic Auth or will use JWTs later).
                        APIs are meant to be stateless, and CSRF protection assumes session-based authentication.
                        CSRF tokens don’t play well with tools like Postman or mobile apps.
                        Basic Auth and JWT are not vulnerable to CSRF unless you start using cookies for authentication —
                        and you’re not.
                    */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {

                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
        AuthenticationProvider	Authenticates a user using your DB + encoder
        DaoAuthenticationProvider	Default provider for username/password login
        UserDetailsService	Loads user from your database
        PasswordEncoder	Hashes and verifies passwords
        AuthenticationManager	Orchestrates the authentication process
     */
}
