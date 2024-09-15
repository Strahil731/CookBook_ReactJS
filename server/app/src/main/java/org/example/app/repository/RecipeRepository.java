package org.example.app.repository;

import org.example.app.model.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeRepository extends JpaRepository<RecipeEntity, UUID> {
}
