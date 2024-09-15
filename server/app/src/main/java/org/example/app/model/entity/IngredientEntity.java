package org.example.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ingredients")
@NoArgsConstructor
@AllArgsConstructor
public class IngredientEntity extends BaseEntity {
    @Column
    private String name;

    @Column
    private String quantity;

    @ManyToOne(targetEntity = RecipeEntity.class)
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeId;
}
