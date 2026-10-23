package com.ruru.smartpantry;

/** Stores one ingredient that the user has in the pantry. */
public class PantryItem {

    private final long id;
    private final String name;
    private final double quantity;
    private final String unit;
    private final String expiryDate;

    /** Creates a pantry item with its saved database values. */
    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    /** Returns the database identifier. */
    public long getId() {
        return id;
    }

    /** Returns the ingredient name. */
    public String getName() {
        return name;
    }

    /** Returns the available quantity. */
    public double getQuantity() {
        return quantity;
    }

    /** Returns the unit used for the quantity. */
    public String getUnit() {
        return unit;
    }

    /** Returns the optional expiry date. */
    public String getExpiryDate() {
        return expiryDate;
    }
}
