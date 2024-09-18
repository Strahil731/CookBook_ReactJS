package org.example.app.repository;

import org.example.app.model.entity.IngredientEntity;
import org.example.app.model.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<IngredientEntity, UUID> {
    @Modifying
    void deleteAllByRecipeId(RecipeEntity recipe);
}
