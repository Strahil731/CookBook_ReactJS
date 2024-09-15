package org.example.app.model.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCreateForm {
    private String title;

    private String preparation;

    private String imageUrl;

    private String ingredients;

    private Long userId;
}
