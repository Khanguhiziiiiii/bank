package org.khanguhizi.bankmanagementsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.khanguhizi.bankmanagementsystem.service.CustomUserDetailsService;
import org.khanguhizi.bankmanagementsystem.service.JWTService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public interface JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private  final CustomUserDetailsService userDetailsService;

    public JWTAuthenticationFilter(JWTService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    protected void doFilterInternalServer(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException{

    }
}
