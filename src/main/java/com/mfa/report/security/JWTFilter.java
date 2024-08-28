package com.mfa.report.security;

import com.mfa.report.service.JWTService;
import com.mfa.report.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String token = null;
        String email = null;
        if (!Objects.isNull(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            token = authHeader.substring(7);
            email = jwtService.extractEmail(token);
        }

        // allow sign up and sign in to process even without jwt
        if (Objects.isNull(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!Objects.isNull(email)
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
