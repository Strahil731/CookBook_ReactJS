package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.example.app.model.dto.recipe.RecipeCreateForm;
import org.example.app.model.dto.recipe.RecipeDto;
import org.example.app.model.entity.IngredientEntity;
import org.example.app.model.entity.RecipeEntity;
import org.example.app.model.entity.UserEntity;
import org.example.app.repository.IngredientRepository;
import org.example.app.repository.RecipeRepository;
import org.example.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    @Cacheable("recipes")
    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream().map(RecipeDto::mapToRecipeDto).toList();
    }

    public RecipeDto getRecipeById(UUID id) {
        return this.recipeRepository.findById(id)
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .orElse(null);
    }

    public String addRecipe(RecipeCreateForm recipeCreateForm, String userEmail) {
        UserEntity user = this.userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with doesn't exist!"));

        RecipeEntity recipeEntity = createRecipeEntity(recipeCreateForm, user);
        recipeEntity = this.recipeRepository.save(recipeEntity);

        List<IngredientEntity> ingredients = extractIngredients(recipeCreateForm, recipeEntity);
        this.ingredientRepository.saveAll(ingredients);

        return recipeEntity.getId().toString();
    }

    private List<IngredientEntity> extractIngredients(RecipeCreateForm recipeCreateForm, RecipeEntity recipe) {
        String[] arr = recipeCreateForm.getIngredients().split("\\r?\\n");
        List<IngredientEntity> ingredients = new ArrayList<>();

        for (String ingredient : arr) {
            String name = ingredient.split("-")[0];
            String quantity = ingredient.split("-")[1];
            ingredients.add(new IngredientEntity(name, quantity, recipe));
        }

        return ingredients;
    }

    private RecipeEntity createRecipeEntity(RecipeCreateForm recipeCreateForm, UserEntity user) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setCreatedAt(LocalDateTime.now());
        recipeEntity.setOwner(user);
        recipeEntity.setTitle(recipeCreateForm.getTitle());
        recipeEntity.setImageUrl(recipeCreateForm.getImageUrl());
        recipeEntity.setPreparation(recipeCreateForm.getPreparation());

        return recipeRepository.save(recipeEntity);
    }

    @CacheEvict("recipes")
    public void refreshRecipes() {
    }
}
