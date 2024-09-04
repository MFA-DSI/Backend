package com.mfa.report.endpoint.rest;


import com.mfa.report.endpoint.rest.model.Exception;
import com.mfa.report.repository.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InternalToRestException {
    @ExceptionHandler(value = {BadRequestException.class})
    ResponseEntity<Exception> handleBadRequest(
            BadRequestException e) {
        return new ResponseEntity<>(toRest(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Exception> handleNotFound(NotFoundException e) {
        return new ResponseEntity<>(toRest(e, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Exception> handleUnauthorized(ForbiddenException e) {
        return new ResponseEntity<>(toRest(e, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(MethodNotSupportedException.class)
    public ResponseEntity<Exception> handleMethodNotAllowed(MethodNotSupportedException e) {
        return new ResponseEntity<>(toRest(e, HttpStatus.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
    }

    private Exception toRest(ApiException e, HttpStatus status) {
        var restException = new Exception();
        restException.setType(status.toString());
        restException.setMessage(e.getMessage());
        return restException;
    }

}
