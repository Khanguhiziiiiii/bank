package org.khanguhizi.bankmanagementsystem.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.khanguhizi.bankmanagementsystem.service.CustomUserDetailsService;
import org.khanguhizi.bankmanagementsystem.service.JWTService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private  final CustomUserDetailsService userDetailsService;

    public JWTAuthenticationFilter(JWTService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain

            /*
                You use it to inspect the request and decide whether to authenticate it.
                    Parameters:
                        request → incoming HTTP request
                        response → outgoing HTTP response
                        chain → allows the request to continue to the next filter
             */
    ) throws ServletException, IOException{

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        /*
            Tries to get the JWT from the HTTP Authorization header.
            Also declares two variables (jwt and username) for later use.
         */

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }else{
            jwt = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(jwt);
            } catch (Exception e) {
                chain.doFilter(request, response);
                return;
                // Token invalid or expired
                // Log if needed, but do not stop the chain
            }
        }

        /*
            If:
                The header is missing, or
                It does not start with "Bearer "
            Then the filter skips JWT processing and simply continues the filter chain.
            This is important because routes like /login or /register may not send a token.
         */


        //jwt = authHeader.substring(7);
        //username = jwtService.extractUsername(jwt);

        /*
            Removes the "Bearer " prefix (which has 7 characters) to get the actual token
            Uses your JWTService to decode the token and extract the username (stored in the JWT “subject” field).
         */

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            /*
                This ensures:
                    The token has a valid username.
                    The user isn’t already authenticated in the current request (to avoid re-authenticating).
                Loads the user’s full information (from database or memory) using your CustomUserDetailsService.
                    The returned UserDetails object contains:
                    username
                    password (usually not used here)
                    roles/authorities
             */


            if (jwtService.isTokenValid(jwt, userDetails)) {

                //  Read all claims from the token
                Claims claims = jwtService.extractAllClaims(jwt);

                // Extract authorities list
                List<?> roles = claims.get("authorities", List.class);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(Object::toString)           // Force string conversion
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                //  Create authentication token with JWT authorities
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities   // 🚀 use JWT authorities, not DB roles.
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //  Store authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }


        chain.doFilter(request, response);

        /*
            Passes the request onward to the next filter or the controller.
            Required for the request to continue processing.
         */
    }
}
