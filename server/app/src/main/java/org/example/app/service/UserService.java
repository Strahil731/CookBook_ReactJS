package org.example.app.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.app.model.dto.user.*;
import org.example.app.model.entity.UserEntity;
import org.example.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final HttpServletResponse response;

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

        String token = jwtService.generateToken(userDto.getEmail());
        response.addHeader("Authorization", "Bearer " + token);

        return new RegisterResponse(userDto, HttpStatus.CREATED);
    }

    public LoginResponse login(UserLoginForm userLoginForm) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                authenticationProvider
                        .authenticate(new UsernamePasswordAuthenticationToken(userLoginForm.email(), userLoginForm.password()));

            } catch (AuthenticationException e) {
                return new LoginResponse(null, HttpStatus.UNAUTHORIZED);
            }
        }

        UserDto user = modelMapper.map(this.userRepository.findByEmail(userLoginForm.email()).orElseThrow(), UserDto.class);

        response.addHeader("Authorization", "Bearer " + this.jwtService.generateToken(user.getEmail()));

        return new LoginResponse(user, HttpStatus.OK);
    }
}
