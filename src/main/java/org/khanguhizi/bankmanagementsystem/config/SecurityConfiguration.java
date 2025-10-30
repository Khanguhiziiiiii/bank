package org.khanguhizi.bankmanagementsystem.config;


import jakarta.servlet.http.HttpServletResponse;
import org.khanguhizi.bankmanagementsystem.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

@Configuration        //marks this as a Spring configuration class (Spring loads and runs it at startup).
@EnableWebSecurity    //enables Spring Security’s web security support.
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfiguration(JWTAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                                //.anyRequest().authenticated()
                        .requestMatchers(
                                "/createAccountType"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/withdraw",
                                "/deposit",
                                "/transferFunds",
                                "/checkBalance",
                                "/isOverdraftOptedIn"
                        ).hasRole("USER")
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                ) //all other requests require jwt authentication
                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) //make sessions stateless

                .authenticationProvider(authenticationProvider())


                .exceptionHandling(ex -> ex

                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized: Invalid or missing token.\"}");
                        })

                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Forbidden: Insufficient permissions.\"}");
                        })
                )


                .build();// Build and return the SecurityFilterChain
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
                /*
                    Creates a new CorsConfiguration object.
                    This is where you define which origins, headers, and methods are allowed to call your API.
                 */
        config.setAllowCredentials(false);
                /*
                    This controls whether browsers are allowed to include credentials (cookies, Authorization headers) in cross-origin requests.
                    true → Allows cookies / authorization headers to be sent cross-origin (secure, specific origins only).
                    false → No credentials allowed; can use wildcard * origin.
                    Since it’s set to false, Spring can safely use Access-Control-Allow-Origin: *.
                    If this were true, you’d need to specify explicit origins instead of *.
                 */
        config.addAllowedOriginPattern("*");
                /*
                    Defines which origins (domains) are allowed to make requests to your backend.
                    * means:
                    “Allow requests from any domain.”
                    Using addAllowedOriginPattern (instead of addAllowedOrigin) allows for wildcard patterns like https://*.example.com.
                 */
        config.addAllowedMethod("*");
                /*
                    Defines which HTTP methods (GET, POST, PUT, DELETE, etc.) are allowed.
                    * means:
                    “Allow all HTTP methods.”
                    Example: If your frontend sends a POST request, this tells Spring: “That’s fine, let it through.”
                 */
        config.addAllowedHeader("*");
                /*
                    Defines which HTTP headers can be sent in cross-origin requests.
                    * means:
                    “Allow any custom headers.”
                    This is important if your frontend sends headers like:
                    Authorization: Bearer <token>
                    Content-Type: application/json
                 */
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Type");
                /*
                    Defines which headers are visible to the frontend (JavaScript code in the browser) after a response.
                    Normally, browsers only expose a few safe headers (like Content-Length).
                    This tells the browser:
                    “It’s okay to let frontend code read the values of these headers.”
                    For example, if your backend returns an Authorization header (like a new JWT), the frontend can now
                    access it via response.headers.get("Authorization").
                 */
        config.setMaxAge(3600L);
                /*
                    Specifies how long (in seconds) browsers can cache the CORS preflight response.
                    3600L = 1 hour.
                    So the browser won’t repeat the OPTIONS preflight request for every API call — it will remember
                    the CORS approval for an hour.
                 */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                /*
                    This object maps URL patterns to specific CorsConfiguration rules.
                    You can use this to apply different rules to different endpoints (if you wanted).
                 */
        source.registerCorsConfiguration("/**", config);
                /*
                    Registers your CorsConfiguration to apply to all endpoints (/**).
                    So it doesn’t matter if the request is /login, /register, or /createAccount — they’ll all use
                    this CORS policy.
                 */
        return source;
                /*
                    Returns the configured CorsConfigurationSource to Spring.
                    Spring Security will use it whenever a request is made, automatically applying the correct headers.
                 */
    }

    /*
        This method tells Spring Security:
        “For every request, allow all domains, headers, and methods to access the API, and include the Authorization
        and Content-Type headers in responses. Don’t allow cookies, and remember this CORS setup for one hour.”
     */

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}