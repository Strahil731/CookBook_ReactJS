package org.example.app.model.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.app.model.entity.IngredientEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private String name;

    private String quantity;

    public static IngredientDto mapToIngredientDto(IngredientEntity ingredientEntity) {
        return new IngredientDto(ingredientEntity.getName(), ingredientEntity.getQuantity());
    }
}
