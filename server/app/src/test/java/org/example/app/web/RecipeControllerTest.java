package org.example.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.model.dto.recipe.RecipeCreateForm;
import org.example.app.model.dto.user.UserLoginForm;
import org.example.app.model.entity.RecipeEntity;
import org.example.app.model.entity.UserEntity;
import org.example.app.repository.RecipeRepository;
import org.example.app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RecipeControllerTest {
    private static final String DEFAULT_EMAIL = "kalin@abv.bg";
    private static final String DEFAULT_PASSWORD = "12345";

    private static String recipeId1 = "";
    private static String ownerId = "";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        UserEntity user = new UserEntity();
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user = userRepository.save(user);
        ownerId = user.getId().toString();

        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setTitle("Pizza");
        recipe1.setOwner(user);
        recipeId1 = recipeRepository.save(recipe1).getId().toString();

        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setTitle("French fries");
        recipe2.setOwner(user);
        recipeRepository.save(recipe2);

    }

    @AfterEach
    public void tearDown() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllRecipes() throws Exception {
        mockMvc.perform(get("/api/recipe")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].userId").value(ownerId))
                .andExpect(jsonPath("$.[1].userId").value(ownerId));
    }

    @Test
    public void testGetRecipeById() throws Exception {
        mockMvc.perform(get("/api/recipe/{id}", recipeId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Pizza"))
                .andExpect(jsonPath("$.userId").value(ownerId));
    }

    @Test
    public void testGetRecipeWithWrongId() throws Exception {
        mockMvc.perform(get("/api/recipe/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, password = DEFAULT_EMAIL)
    public void testCreateRecipe() throws Exception {
        RecipeCreateForm recipe = createTestRecipe();

        String jsonRecipe = new ObjectMapper().writeValueAsString(recipe);

        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRecipe))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, password = DEFAULT_EMAIL)
    public void testCreateRecipeWithWrongData() throws Exception {
        RecipeCreateForm recipe = createTestRecipe();
        recipe.setTitle("");

        String jsonRecipe = new ObjectMapper().writeValueAsString(recipe);

        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRecipe))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].fieldName").value("title"));

    }

    @Test
    public void testDeleteRecipe() {
        HttpHeaders headers = getTokenHeader(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/api/recipe/{id}", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class, recipeId1);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertEquals(1, recipeRepository.count());
    }

    @Test
    public void testDeleteRecipeWithWrongId() {
        HttpHeaders headers = getTokenHeader(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/api/recipe/{id}", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class, UUID.randomUUID());

        assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());
    }

    @Test
    public void testDeleteRecipeWithWrongUser() {
        UserEntity user = new UserEntity();
        user.setEmail("pesho@abv.bg");
        user.setPassword(passwordEncoder.encode("12345"));
        userRepository.save(user);

        HttpHeaders headers = getTokenHeader("pesho@abv.bg", "12345");

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/api/recipe/{id}", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class, recipeId1);

        assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());
    }

    @Test
    public void testUpdateRecipe() {
        HttpHeaders headers = getTokenHeader(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        RecipeCreateForm recipe = createTestRecipe();

        RecipeEntity beforeUpdate = recipeRepository.findById(UUID.fromString(recipeId1)).orElse(null);
        assertNotNull(beforeUpdate);
        assertEquals(0, beforeUpdate.getIngredients().size());
        assertNull(beforeUpdate.getImageUrl());
        assertNull(beforeUpdate.getPreparation());

        ResponseEntity<Void> response = restTemplate.exchange("/api/recipe/{id}", HttpMethod.PUT, new HttpEntity<>(recipe, headers), Void.class, recipeId1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        RecipeEntity afterUpdate = recipeRepository.findById(UUID.fromString(recipeId1)).orElse(null);
        assertNotNull(afterUpdate);
        assertEquals(1, afterUpdate.getIngredients().size());
        assertEquals("url", afterUpdate.getImageUrl());
        assertEquals("Cook", afterUpdate.getPreparation());

    }

    @Test
    public void testUpdateRecipeWithWrongId() {
        HttpHeaders headers = getTokenHeader(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        RecipeCreateForm recipe = createTestRecipe();

        ResponseEntity<Void> response = restTemplate.exchange("/api/recipe/{id}", HttpMethod.PUT, new HttpEntity<>(recipe, headers), Void.class, UUID.randomUUID());

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        RecipeEntity beforeUpdate = recipeRepository.findById(UUID.fromString(recipeId1)).orElse(null);
        assertNotNull(beforeUpdate);
        assertEquals(0, beforeUpdate.getIngredients().size());
        assertNull(beforeUpdate.getImageUrl());
        assertNull(beforeUpdate.getPreparation());

    }

    @Test
    public void testUpdateRecipeWithWrongUser() {
        UserEntity user = new UserEntity();
        user.setEmail("pesho@abv.bg");
        user.setPassword(passwordEncoder.encode("12345"));
        userRepository.save(user);

        HttpHeaders headers = getTokenHeader("pesho@abv.bg", DEFAULT_PASSWORD);

        RecipeCreateForm recipe = createTestRecipe();

        ResponseEntity<Void> response = restTemplate.exchange("/api/recipe/{id}", HttpMethod.PUT, new HttpEntity<>(recipe, headers), Void.class, recipeId1);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        RecipeEntity beforeUpdate = recipeRepository.findById(UUID.fromString(recipeId1)).orElse(null);
        assertNotNull(beforeUpdate);
        assertEquals(0, beforeUpdate.getIngredients().size());
        assertNull(beforeUpdate.getImageUrl());
        assertNull(beforeUpdate.getPreparation());

    }

    @Test
    public void testUpdateRecipeWithWrongData() {
        HttpHeaders headers = getTokenHeader(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        RecipeCreateForm recipe = createTestRecipe();
        recipe.setTitle("");

        ResponseEntity<Void> response = restTemplate.exchange("/api/recipe/{id}", HttpMethod.PUT, new HttpEntity<>(recipe, headers), Void.class, recipeId1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        RecipeEntity beforeUpdate = recipeRepository.findById(UUID.fromString(recipeId1)).orElse(null);
        assertNotNull(beforeUpdate);
        assertEquals(0, beforeUpdate.getIngredients().size());
        assertNull(beforeUpdate.getImageUrl());
        assertNull(beforeUpdate.getPreparation());

    }

    private RecipeCreateForm createTestRecipe() {
        RecipeCreateForm recipe = new RecipeCreateForm();
        recipe.setTitle("Pizza");
        recipe.setImageUrl("url");
        recipe.setPreparation("Cook");
        recipe.setIngredients("dough-1kg");

        return recipe;
    }


    private HttpHeaders getTokenHeader(String email, String password) {
        UserLoginForm login = new UserLoginForm(email, password);

        ResponseEntity<Object> response = restTemplate.postForEntity("/api/auth/login", login, Object.class);
        String token = response.getHeaders().get("Authorization").getFirst();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        return headers;
    }
}
