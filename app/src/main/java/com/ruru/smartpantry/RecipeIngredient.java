package com.ruru.smartpantry;

/** Stores one ingredient required by a recipe. */
public class RecipeIngredient {

    private final String name;
    private final double quantity;
    private final String unit;

    /** Creates a recipe ingredient. */
    public RecipeIngredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    /** Returns the ingredient name. */
    public String getName() {
        return name;
    }

    /** Returns the required quantity. */
    public double getQuantity() {
        return quantity;
    }

    /** Returns the measurement unit. */
    public String getUnit() {
        return unit;
    }
}
