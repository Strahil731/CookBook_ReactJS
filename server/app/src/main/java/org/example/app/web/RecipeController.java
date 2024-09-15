package org.example.app.web;

import lombok.RequiredArgsConstructor;
import org.example.app.model.dto.FieldErrorDto;
import org.example.app.model.dto.recipe.RecipeCreateForm;
import org.example.app.model.dto.recipe.RecipeDto;
import org.example.app.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        return ResponseEntity.ok(this.recipeService.getAllRecipes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable UUID id) {
        return ResponseEntity.ok(this.recipeService.getRecipeById(id));
    }

    @PostMapping
    public ResponseEntity<Object> createRecipe(@Validated @RequestBody RecipeCreateForm recipeCreateForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<FieldErrorDto> errors = new ArrayList<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(new FieldErrorDto(error.getField(), error.getDefaultMessage()));
            }

            return ResponseEntity.badRequest().body(errors);
        }

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(this.recipeService.addRecipe(recipeCreateForm, principal.getName()))
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
