package com.mfa.report.repository.exception;

public class MethodNotSupportedException extends ApiException{
    public MethodNotSupportedException(String message){super(ExceptionType.CLIENT_EXCEPTION,message);}
}
