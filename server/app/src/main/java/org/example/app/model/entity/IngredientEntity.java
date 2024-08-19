package org.example.app.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ingredients")
public class IngredientEntity extends BaseEntity {
    @ManyToOne(targetEntity = RecipeEntity.class)
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeId;

    @Column
    private String name;

    @Column
    private String quantity;
}
