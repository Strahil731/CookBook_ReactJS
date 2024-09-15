package org.example.app.model.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.model.dto.ingredient.IngredientDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id;

    private String title;

    private String preparation;

    private String imageUrl;

    private List<IngredientDto> ingredients;

    private Long userId;
}
