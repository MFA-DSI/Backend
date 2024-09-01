package com.mfa.report.service.Auth;

import com.mfa.report.endpoint.rest.Credentials;
import com.mfa.report.endpoint.rest.model.Auth;
import com.mfa.report.endpoint.rest.model.SignUp;
import com.mfa.report.endpoint.rest.model.Token;

public interface AuthService {
    public Token authenticate (Credentials auth);
}
