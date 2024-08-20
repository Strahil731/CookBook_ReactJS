package org.example.app.web;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleJwtException(Exception e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());

        problemDetail.setProperty("reason", "Invalid or expired token");

        return problemDetail;
    }
}
