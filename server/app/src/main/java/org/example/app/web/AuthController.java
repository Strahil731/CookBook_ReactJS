package org.example.app.web;

import lombok.RequiredArgsConstructor;
import org.example.app.model.dto.user.*;
import org.example.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterForm userRegisterForm) {
        RegisterResponse response = this.userService.registerUser(userRegisterForm);

        return new ResponseEntity<>(response.userDto(), response.status());
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserLoginForm userLoginForm) {
        LoginResponse response = this.userService.login(userLoginForm);

        return new ResponseEntity<>(response.user(), response.status());
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
