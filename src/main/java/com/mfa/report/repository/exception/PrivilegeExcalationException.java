package com.mfa.report.repository.exception;

import org.springframework.security.core.AuthenticationException;

public class PrivilegeExcalationException extends ApiException {
    public PrivilegeExcalationException(String message) {
        super(ExceptionType.CLIENT_EXCEPTION, message);
    }
}
