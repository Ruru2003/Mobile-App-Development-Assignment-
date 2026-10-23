package com.ruru.smartpantry;

import java.util.ArrayList;
import java.util.List;

/** Stores a recipe and the ingredients needed to make it. */
public class Recipe {

    private final long id;
    private final String name;
    private final String instructions;
    private final List<RecipeIngredient> ingredients = new ArrayList<>();

    /** Creates a recipe. */
    public Recipe(long id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    /** Adds one required ingredient. */
    public void addIngredient(RecipeIngredient ingredient) {
        ingredients.add(ingredient);
    }

    /** Returns the database identifier. */
    public long getId() {
        return id;
    }

    /** Returns the recipe name. */
    public String getName() {
        return name;
    }

    /** Returns the cooking instructions. */
    public String getInstructions() {
        return instructions;
    }

    /** Returns a copy of the required ingredients. */
    public List<RecipeIngredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }
}
