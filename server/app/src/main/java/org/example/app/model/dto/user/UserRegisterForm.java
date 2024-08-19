package org.example.app.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterForm {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
