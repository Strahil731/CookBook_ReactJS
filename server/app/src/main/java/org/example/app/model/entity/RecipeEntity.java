package org.example.app.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "recipes")
@DynamicUpdate
public class RecipeEntity extends BaseEntity {
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity owner;

    @Column
    private String title;

    @Column
    private String preparation;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipeId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<IngredientEntity> ingredients;

}
