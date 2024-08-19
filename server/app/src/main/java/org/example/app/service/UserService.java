package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.example.app.model.dto.user.RegisterResponse;
import org.example.app.model.dto.user.UserDto;
import org.example.app.model.dto.user.UserRegisterForm;
import org.example.app.model.entity.UserEntity;
import org.example.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse registerUser(UserRegisterForm userRegisterForm) {
        if (this.userRepository.findByEmail(userRegisterForm.getEmail()).isPresent()) {
            return new RegisterResponse(null, HttpStatus.CONFLICT);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRegisterForm.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userRegisterForm.getPassword()));
        userEntity.setFirstName(userRegisterForm.getFirstName());
        userEntity.setLastName(userRegisterForm.getLastName());
        userEntity.setCreatedAt(LocalDateTime.now());


        UserDto userDto = modelMapper.map(this.userRepository.save(userEntity), UserDto.class);

        return new RegisterResponse(userDto, HttpStatus.CREATED);
    }
}
