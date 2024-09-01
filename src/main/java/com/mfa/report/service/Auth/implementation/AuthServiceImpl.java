package com.mfa.report.service.Auth.implementation;

import com.mfa.report.endpoint.rest.Credentials;
import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.endpoint.rest.model.Token;
import com.mfa.report.service.Auth.AuthService;
import com.mfa.report.service.Auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private  final TokenService tokenService;
    @Override
    public Token authenticate(Credentials auth) {
        return tokenService.generateToken(auth.getAuthority(),auth.getEmail());
    }

}
