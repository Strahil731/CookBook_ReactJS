package org.example.app.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserLoginForm(@Email String email, @Size(min = 5, max = 30) String password) {
}
