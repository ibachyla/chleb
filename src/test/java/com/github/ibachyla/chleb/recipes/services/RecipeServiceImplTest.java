package com.github.ibachyla.chleb.recipes.services;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.models.validators.EntityValidator;
import com.github.ibachyla.chleb.models.validators.ValidationException;
import com.github.ibachyla.chleb.recipes.TestValues;
import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import com.github.ibachyla.chleb.recipes.data.mappers.RecipeFromPersistenceEntityMapper;
import com.github.ibachyla.chleb.recipes.data.mappers.RecipeToPersistenceEntityMapper;
import com.github.ibachyla.chleb.recipes.data.repositories.RecipeRepository;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.services.exceptions.RecipeNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class RecipeServiceImplTest {

  @Mock
  RecipeRepository recipeRepositoryMock;

  @Mock
  EntityValidator<Recipe> recipeValidatorMock;

  RecipeServiceImpl recipeService;

  @BeforeEach
  void setUp() {
    recipeService = new RecipeServiceImpl(recipeRepositoryMock,
        recipeValidatorMock,
        new RecipeFromPersistenceEntityMapper(),
        new RecipeToPersistenceEntityMapper());
  }

  @Test
  void createRecipe_null() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> recipeService.createRecipe(null));
    assertEquals("recipe cannot be null", ex.getMessage());
  }

  @Test
  void createRecipe_invalid() {
    // Arrange
    Recipe recipe = TestValues.recipe();

    doThrow(new ValidationException("invalid")).when(recipeValidatorMock).validate(recipe);

    // Act & Assert
    assertThatThrownBy(() -> recipeService.createRecipe(recipe))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void createRecipe() {
    // Arrange
    Recipe recipe = TestValues.recipe();

    RecipeEntity recipeEntity = new RecipeEntity();
    recipeEntity.id(recipe.id());
    recipeEntity.name(recipe.name().value());
    recipeEntity.slug(recipe.slug().value());
    recipeEntity.createdAt(recipe.createdAt().getEpochSecond());
    recipeEntity.updatedAt(recipe.updatedAt().getEpochSecond());

    when(recipeRepositoryMock.save(argThat(entity -> entity.id().equals(recipeEntity.id()))))
        .thenReturn(recipeEntity);

    // Act
    Recipe createdRecipe = recipeService.createRecipe(recipe);

    // Assert
    assertEquals(recipe, createdRecipe);
  }

  @Test
  void getRecipe_null() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> recipeService.getRecipe(null));
    assertEquals("slug or id cannot be blank", ex.getMessage());
  }

  @Test
  void getRecipe_blank() {
    // Act & Assert
    Exception ex = assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipe(""));
    assertEquals("slug or id cannot be blank", ex.getMessage());
  }

  @Test
  void getRecipe_bySlug_notFound() {
    // Arrange
    String slug = TestValues.recipeSlug().value();
    when(recipeRepositoryMock.findBySlugOrId(slug, null)).thenReturn(Optional.empty());

    // Act & Assert
    Exception ex = assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipe(slug));
    assertEquals("Recipe with slug or id " + slug + " not found", ex.getMessage());
  }

  @Test
  void getRecipe_bySlug() {
    // Arrange
    RecipeEntity recipeEntity = TestValues.recipeEntity();

    when(recipeRepositoryMock.findBySlugOrId(recipeEntity.slug(), null)).thenReturn(
        Optional.of(recipeEntity));

    // Act
    Recipe recipe = recipeService.getRecipe(recipeEntity.slug());

    // Assert
    assertEquals(recipeEntity.id(), recipe.id());
    assertEquals(recipeEntity.name(), recipe.name().value());
    assertEquals(recipeEntity.slug(), recipe.slug().value());
    assertEquals(Instant.ofEpochSecond(recipeEntity.createdAt()), recipe.createdAt());
    assertEquals(Instant.ofEpochSecond(recipeEntity.updatedAt()), recipe.updatedAt());
  }

  @Test
  void getRecipe_byId_notFound() {
    // Arrange
    UUID id = randomUUID();
    when(recipeRepositoryMock.findBySlugOrId(id.toString(), id)).thenReturn(Optional.empty());

    // Act & Assert
    Exception ex =
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipe(id.toString()));
    assertEquals("Recipe with slug or id " + id + " not found", ex.getMessage());
  }

  @Test
  void getRecipe_byId() {
    // Arrange
    RecipeEntity recipeEntity = TestValues.recipeEntity();

    when(recipeRepositoryMock.findBySlugOrId(recipeEntity.id().toString(), recipeEntity.id()))
        .thenReturn(Optional.of(recipeEntity));

    // Act
    Recipe recipe = recipeService.getRecipe(recipeEntity.id().toString());

    // Assert
    assertEquals(recipeEntity.id(), recipe.id());
    assertEquals(recipeEntity.name(), recipe.name().value());
    assertEquals(recipeEntity.slug(), recipe.slug().value());
    assertEquals(Instant.ofEpochSecond(recipeEntity.createdAt()), recipe.createdAt());
    assertEquals(Instant.ofEpochSecond(recipeEntity.updatedAt()), recipe.updatedAt());
  }
}
