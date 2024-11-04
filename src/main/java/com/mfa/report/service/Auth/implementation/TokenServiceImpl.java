package com.mfa.report.service.Auth.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mfa.report.model.enumerated.Role;
import com.mfa.report.endpoint.rest.model.Token;
import com.mfa.report.service.Auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final Algorithm algorithm;

  @Value("${jwt.issuer}")
  private String issuer;

  @Value("${jwt.expiration}")
  private long expiryOffset;

  @Override
  public Token generateToken(Role role, String isStaff) {

    String jwtToken =
        JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject("report-app-token")
                .withIssuer(issuer)
                .withAudience("direction-audience")
                .withClaim("role", List.of(role.toString()))
                .withClaim("isStaff", isStaff )
                .withExpiresAt(new Date().toInstant().plusMillis(expiryOffset))
                .sign(algorithm);

    return Token.builder().accessToken(jwtToken).build();
  }
}
