package org.example.app.web;

import lombok.RequiredArgsConstructor;
import org.example.app.model.dto.FieldErrorDto;
import org.example.app.model.dto.user.*;
import org.example.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Validated @RequestBody UserRegisterForm userRegisterForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldErrorDto> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        RegisterResponse response = this.userService.registerUser(userRegisterForm);

        return new ResponseEntity<>(response.userDto(), response.status());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody UserLoginForm userLoginForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldErrorDto> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        LoginResponse response = this.userService.login(userLoginForm);

        return new ResponseEntity<>(response.user(), response.status());
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
