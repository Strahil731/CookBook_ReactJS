package org.example.app.web;

import lombok.RequiredArgsConstructor;
import org.example.app.model.dto.user.RegisterResponse;
import org.example.app.model.dto.user.UserDto;
import org.example.app.model.dto.user.UserRegisterForm;
import org.example.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
