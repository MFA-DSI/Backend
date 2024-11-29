package com.mfa.report.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
@Component
@Service
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {


  private final JWTVerifier jwtVerifier;

  @Autowired
  private Bucket4jRateLimiter rateLimiter;


  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    log.debug("Authorization Header: " + authorizationHeader); // Debug

    if (authorizationHeader == null || !authorizationHeader.toLowerCase().startsWith("bearer ")) {
      log.debug("Authorization header absent ou invalide");
      filterChain.doFilter(request, response);
      return;
    }
    if (!rateLimiter.tryConsume(request, response)) {
      return; // If rate limit exceeded, stop further processing
    }

    String token = authorizationHeader.substring(7);

    try {
      DecodedJWT decodedJWT = jwtVerifier.verify(token);

      Date expiresAt = decodedJWT.getExpiresAt();
      if (expiresAt != null && expiresAt.before(new Date())) {
        throw new JWTVerificationException("Token has expired");
      }

      List<String> roles = decodedJWT.getClaim("role").asList(String.class);
      log.debug("Rôles extraits du token: " + roles);

      String userId = String.valueOf(decodedJWT.getClaim("userId"));
      log.debug("User ID extrait du token: " + userId);

      List<SimpleGrantedAuthority> rolesList = roles.stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .toList();

      UsernamePasswordAuthenticationToken user =
              new UsernamePasswordAuthenticationToken(decodedJWT, token, rolesList);
      SecurityContextHolder.getContext().setAuthentication(user);

    } catch (JWTVerificationException jwtException) {
      log.error("JWT Verification failed", jwtException);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    filterChain.doFilter(request, response);
  }

}
