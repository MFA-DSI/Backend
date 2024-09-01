package com.mfa.report.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JWTConfig {

  @Value("${jwt.issuer}")
  private String issuer;

  @Value("${jwt.leeway}")
  private long leeway;

  @Value("${jwt.secret}")
  private String jwtKey;

  @Bean
  public JWTVerifier getJWTVerifier(Algorithm algorithm){
    return JWT.require(algorithm)
            .acceptLeeway(leeway)
            .withIssuer(issuer)
            .build();
  }

  @Bean
  public  Algorithm getJWTAlgorithm(){
    return Algorithm.HMAC512(jwtKey);
  }
}
