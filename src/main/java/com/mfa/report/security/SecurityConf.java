package com.mfa.report.security;

import static org.springframework.http.HttpMethod.GET;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConf {
  private final JWTFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .cors()  // Active la gestion CORS dans Spring Security
            .and()
            .csrf().disable()
            .sessionManagement(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (auth) -> {
              auth.requestMatchers("/users/login")
                  .permitAll()
                  .requestMatchers("/users/signup")
                  .permitAll()
                  .requestMatchers("/ping")
                  .permitAll()
                  .requestMatchers(GET, "/direction/all")
                  .permitAll()
                  .anyRequest()
                  .authenticated();
            })
        .exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/error"))
        .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)

        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
