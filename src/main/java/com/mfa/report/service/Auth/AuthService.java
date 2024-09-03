package com.mfa.report.service.Auth;

import com.mfa.report.endpoint.rest.model.Credentials;
import com.mfa.report.endpoint.rest.model.Token;

public interface AuthService {
    public Token authenticate (Credentials auth);
}
