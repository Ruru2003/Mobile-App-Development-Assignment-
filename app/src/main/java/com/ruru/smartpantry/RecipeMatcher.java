package com.ruru.smartpantry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Finds recipes that can be made with the available pantry items. */
public class RecipeMatcher {

    /** Returns recipes for which every ingredient and quantity is available. */
    public List<Recipe> findMakeableRecipes(List<Recipe> recipes,
                                             List<PantryItem> pantryItems) {
        List<Recipe> makeableRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                makeableRecipes.add(recipe);
            }
        }
        return makeableRecipes;
    }

    private boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems) {
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            if (!hasEnoughIngredient(ingredient, pantryItems)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasEnoughIngredient(RecipeIngredient needed,
                                        List<PantryItem> pantryItems) {
        double availableQuantity = 0;
        for (PantryItem pantryItem : pantryItems) {
            if (namesMatch(needed.getName(), pantryItem.getName())
                    && unitsMatch(needed.getUnit(), pantryItem.getUnit())) {
                availableQuantity += convertToBaseQuantity(
                        pantryItem.getQuantity(), pantryItem.getUnit());
            }
        }
        double neededQuantity = convertToBaseQuantity(needed.getQuantity(), needed.getUnit());
        return availableQuantity >= neededQuantity;
    }

    private boolean namesMatch(String firstName, String secondName) {
        return normalizeName(firstName).equals(normalizeName(secondName));
    }

    private String normalizeName(String name) {
        String cleanName = name.trim().toLowerCase(Locale.ROOT);
        if (cleanName.endsWith("ies") && cleanName.length() > 3) {
            return cleanName.substring(0, cleanName.length() - 3) + "y";
        }
        if (cleanName.endsWith("oes") && cleanName.length() > 3) {
            return cleanName.substring(0, cleanName.length() - 2);
        }
        if (cleanName.endsWith("s") && !cleanName.endsWith("ss")) {
            return cleanName.substring(0, cleanName.length() - 1);
        }
        return cleanName;
    }

    private boolean unitsMatch(String firstUnit, String secondUnit) {
        return normalizeUnit(firstUnit).equals(normalizeUnit(secondUnit));
    }

    private String normalizeUnit(String unit) {
        String cleanUnit = unit.trim().toLowerCase(Locale.ROOT);
        if (cleanUnit.equals("kg") || cleanUnit.equals("g")) {
            return "weight";
        }
        if (cleanUnit.equals("l") || cleanUnit.equals("ml")) {
            return "volume";
        }
        if (cleanUnit.equals("piece") || cleanUnit.equals("pieces")) {
            return "each";
        }
        return cleanUnit;
    }

    private double convertToBaseQuantity(double quantity, String unit) {
        String cleanUnit = unit.trim().toLowerCase(Locale.ROOT);
        if (cleanUnit.equals("kg") || cleanUnit.equals("l")) {
            return quantity * 1000;
        }
        return quantity;
    }
}
