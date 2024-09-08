package com.mfa.report.service.Auth;

import com.mfa.report.model.enumerated.Role;
import com.mfa.report.endpoint.rest.model.Token;

public interface TokenService {
    public Token generateToken(Role role,String userId);
}
