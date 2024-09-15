package org.example.app.model.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCreateForm {
    @Size(min = 2, max = 50)
    private String title;

    @Size(min = 3)
    private String preparation;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String ingredients;

    private String userId;
}
