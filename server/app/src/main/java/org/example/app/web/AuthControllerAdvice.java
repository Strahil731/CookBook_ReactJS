package org.example.app.web;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtException() {
        return new ResponseEntity<>("Please login", HttpStatus.UNAUTHORIZED);
    }
}
