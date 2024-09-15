package org.example.app.model.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.model.dto.ingredient.IngredientDto;
import org.example.app.model.entity.RecipeEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private String id;

    private String title;

    private String preparation;

    private String imageUrl;

    private List<IngredientDto> ingredients;

    private String userId;

    public static RecipeDto mapToRecipeDto(RecipeEntity recipeEntity) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipeEntity.getId().toString());
        recipeDto.setTitle(recipeEntity.getTitle());
        recipeDto.setPreparation(recipeEntity.getPreparation());
        recipeDto.setImageUrl(recipeEntity.getImageUrl());
        recipeDto.setIngredients(recipeEntity.getIngredients().stream().map(IngredientDto::mapToIngredientDto).toList());
        recipeDto.setUserId(recipeEntity.getOwner().getId().toString());

        return recipeDto;
    }
}
