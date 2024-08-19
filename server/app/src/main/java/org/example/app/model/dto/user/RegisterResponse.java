package org.example.app.model.dto.user;

import org.springframework.http.HttpStatus;

public record RegisterResponse(UserDto userDto, HttpStatus status) {
}
