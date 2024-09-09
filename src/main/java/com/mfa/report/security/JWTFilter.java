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

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader == null || !authorizationHeader.toLowerCase().startsWith("bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = "";
    if (authorizationHeader.toLowerCase().startsWith("bearer ")) {
      token = authorizationHeader.substring(7);

      try {
        if (StringUtils.isNotBlank(token)) {
          DecodedJWT decodedJWT = jwtVerifier.verify(token);

          Date expiresAt = decodedJWT.getExpiresAt();
          if (expiresAt != null && expiresAt.before(new Date())) {
            throw new JWTVerificationException("Token has expired");
          }

          List<String> roles = decodedJWT.getClaim("role").asList(String.class);
          String userId = String.valueOf(decodedJWT.getClaim("userId"));

          List<SimpleGrantedAuthority> rolesList =
              roles.stream()
                  .map(
                      thisRolesString ->
                          new SimpleGrantedAuthority(
                              String.format("%s%s", "ROLE_", thisRolesString)))
                  .toList();

          UsernamePasswordAuthenticationToken user =
              new UsernamePasswordAuthenticationToken(decodedJWT, token, rolesList);
          SecurityContextHolder.getContext().setAuthentication(user);
        }
      } catch (JWTVerificationException jwtException) {
        log.debug("Problem in token", jwtException);
        throw jwtException;
      }
    }

    filterChain.doFilter(request, response);
  }
}
