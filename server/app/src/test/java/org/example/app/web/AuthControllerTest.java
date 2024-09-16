package org.example.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.model.dto.user.UserLoginForm;
import org.example.app.model.dto.user.UserRegisterForm;
import org.example.app.model.entity.UserEntity;
import org.example.app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser() throws Exception {
        UserRegisterForm userToRegister = new UserRegisterForm();
        userToRegister.setEmail("kalin@abv.bg");
        userToRegister.setPassword("12345");
        userToRegister.setFirstName("Kalin");
        userToRegister.setLastName("Kostadinov");

        String userToJson = new ObjectMapper().writeValueAsString(userToRegister);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userToJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("kalin@abv.bg"))
                .andExpect(jsonPath("$.firstName").value("Kalin"))
                .andExpect(jsonPath("$.lastName").value("Kostadinov"));

        UserEntity registeredUser = userRepository.findByEmail("kalin@abv.bg").orElse(null);

        assertNotNull(registeredUser);
        assertTrue(passwordEncoder.matches(userToRegister.getPassword(), registeredUser.getPassword()));
    }

    @Test
    public void testRegisterUserWithInvalidData() throws Exception {
        UserRegisterForm userToRegister = new UserRegisterForm();
        userToRegister.setEmail("kalin@abv.bg");
        userToRegister.setPassword("123");
        userToRegister.setFirstName("Kalin");
        userToRegister.setLastName("Kostadinov");

        String userToJson = new ObjectMapper().writeValueAsString(userToRegister);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].fieldName").value("password"));
    }

    @Test
    public void testLoginUser() throws Exception {
        UserEntity userToRegister = new UserEntity();
        userToRegister.setEmail("kalin@abv.bg");
        userToRegister.setPassword(passwordEncoder.encode("12345"));
        userToRegister.setFirstName("Kalin");
        userToRegister.setLastName("Kostadinov");

        userRepository.save(userToRegister);

        UserLoginForm userLoginForm = new UserLoginForm("kalin@abv.bg", "12345");
        String userToJson = new ObjectMapper().writeValueAsString(userLoginForm);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userToJson))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(jsonPath("$.email").value("kalin@abv.bg"));

    }

    @Test
    public void testLoginUserWithInvalidData() throws Exception {
        UserEntity userToRegister = new UserEntity();
        userToRegister.setEmail("kalin@abv.bg");
        userToRegister.setPassword(passwordEncoder.encode("12345"));
        userToRegister.setFirstName("Kalin");
        userToRegister.setLastName("Kostadinov");

        userRepository.save(userToRegister);

        UserLoginForm userLoginForm = new UserLoginForm("kalinabv.bg", "12345");
        String userToJson = new ObjectMapper().writeValueAsString(userLoginForm);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].fieldName").value("email"));
    }
}
