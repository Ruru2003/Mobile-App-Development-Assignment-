package com.ruru.smartpantry;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Tests the recipe matching rules. */
public class RecipeMatcherTest {

    private final RecipeMatcher recipeMatcher = new RecipeMatcher();

    /** Confirms a recipe matches when all ingredients are available. */
    @Test
    public void findsRecipeWhenAllIngredientsAreAvailable() {
        Recipe recipe = createRecipe("Cheese Toast",
                new RecipeIngredient("bread", 2, "slice"),
                new RecipeIngredient("cheese", 50, "g"));
        List<PantryItem> pantryItems = Arrays.asList(
                createPantryItem("bread", 4, "slice"),
                createPantryItem("cheese", 100, "g"));

        assertEquals(1, recipeMatcher.findMakeableRecipes(
                Collections.singletonList(recipe), pantryItems).size());
    }

    /** Confirms a missing ingredient prevents a match. */
    @Test
    public void rejectsRecipeWhenIngredientIsMissing() {
        Recipe recipe = createRecipe("Cheese Toast",
                new RecipeIngredient("bread", 2, "slice"),
                new RecipeIngredient("cheese", 50, "g"));

        assertTrue(recipeMatcher.findMakeableRecipes(
                Collections.singletonList(recipe),
                Collections.singletonList(createPantryItem("bread", 4, "slice"))).isEmpty());
    }

    /** Confirms an insufficient quantity prevents a match. */
    @Test
    public void rejectsRecipeWhenQuantityIsTooSmall() {
        Recipe recipe = createRecipe("Rice", new RecipeIngredient("rice", 100, "g"));

        assertTrue(recipeMatcher.findMakeableRecipes(
                Collections.singletonList(recipe),
                Collections.singletonList(createPantryItem("rice", 50, "g"))).isEmpty());
    }

    /** Confirms simple singular and plural names match. */
    @Test
    public void matchesSimplePluralNames() {
        Recipe recipe = createRecipe("Tomato Pasta",
                new RecipeIngredient("tomato", 2, "each"));

        assertEquals(1, recipeMatcher.findMakeableRecipes(
                Collections.singletonList(recipe),
                Collections.singletonList(createPantryItem("tomatoes", 2, "pieces"))).size());
    }

    /** Confirms kilograms and grams can be compared. */
    @Test
    public void convertsKilogramsToGrams() {
        Recipe recipe = createRecipe("Chicken Rice",
                new RecipeIngredient("chicken", 200, "g"));

        assertEquals(1, recipeMatcher.findMakeableRecipes(
                Collections.singletonList(recipe),
                Collections.singletonList(createPantryItem("chicken", 0.5, "kg"))).size());
    }

    private Recipe createRecipe(String name, RecipeIngredient... ingredients) {
        Recipe recipe = new Recipe(1, name, "Test instructions");
        for (RecipeIngredient ingredient : ingredients) {
            recipe.addIngredient(ingredient);
        }
        return recipe;
    }

    private PantryItem createPantryItem(String name, double quantity, String unit) {
        return new PantryItem(1, name, quantity, unit, "");
    }
}
