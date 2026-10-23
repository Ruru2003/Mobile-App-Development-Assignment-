package com.ruru.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Creates and manages the local Smart Pantry SQLite database. */
public class PantryDatabaseHelper extends SQLiteOpenHelper {

    public static final String PANTRY_TABLE = "pantry_items";
    public static final String RECIPE_TABLE = "recipes";
    public static final String RECIPE_INGREDIENT_TABLE = "recipe_ingredients";

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    /** Creates a helper for the local pantry database. */
    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** Enables foreign-key checks whenever the database is opened. */
    @Override
    public void onConfigure(SQLiteDatabase database) {
        super.onConfigure(database);
        database.setForeignKeyConstraintsEnabled(true);
    }

    /** Creates the database tables and inserts the starter recipes. */
    @Override
    public void onCreate(SQLiteDatabase database) {
        createPantryTable(database);
        createRecipeTables(database);
        createSettingsTable(database);
        seedRecipes(database);
    }

    /** Recreates the tables when the database version changes. */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS settings");
        database.execSQL("DROP TABLE IF EXISTS " + RECIPE_INGREDIENT_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + RECIPE_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + PANTRY_TABLE);
        onCreate(database);
    }

    /** Adds an ingredient and returns its new database identifier. */
    public long addPantryItem(String name, double quantity, String unit, String expiryDate) {
        ContentValues values = createPantryValues(name, quantity, unit, expiryDate);
        return getWritableDatabase().insertOrThrow(PANTRY_TABLE, null, values);
    }

    /** Returns all pantry ingredients in alphabetical order. */
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> pantryItems = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(
                PANTRY_TABLE, null, null, null, null, null, "name COLLATE NOCASE")) {
            while (cursor.moveToNext()) {
                pantryItems.add(readPantryItem(cursor));
            }
        }
        return pantryItems;
    }

    /** Returns one pantry ingredient, or null when it does not exist. */
    public PantryItem getPantryItem(long pantryItemId) {
        String selection = "id = ?";
        String[] selectionArguments = {String.valueOf(pantryItemId)};
        try (Cursor cursor = getReadableDatabase().query(
                PANTRY_TABLE, null, selection, selectionArguments, null, null, null)) {
            return cursor.moveToFirst() ? readPantryItem(cursor) : null;
        }
    }

    /** Updates an existing pantry ingredient. */
    public boolean updatePantryItem(long pantryItemId, String name, double quantity,
                                    String unit, String expiryDate) {
        ContentValues values = createPantryValues(name, quantity, unit, expiryDate);
        String[] selectionArguments = {String.valueOf(pantryItemId)};
        int updatedRows = getWritableDatabase().update(
                PANTRY_TABLE, values, "id = ?", selectionArguments);
        return updatedRows == 1;
    }

    /** Deletes a pantry ingredient. */
    public boolean deletePantryItem(long pantryItemId) {
        String[] selectionArguments = {String.valueOf(pantryItemId)};
        int deletedRows = getWritableDatabase().delete(
                PANTRY_TABLE, "id = ?", selectionArguments);
        return deletedRows == 1;
    }

    private ContentValues createPantryValues(String name, double quantity,
                                              String unit, String expiryDate) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);
        return values;
    }

    private PantryItem readPantryItem(Cursor cursor) {
        return new PantryItem(
                cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                cursor.getString(cursor.getColumnIndexOrThrow("expiry_date")));
    }

    private void createPantryTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + PANTRY_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "quantity REAL NOT NULL, "
                + "unit TEXT NOT NULL, "
                + "expiry_date TEXT)");
    }

    private void createRecipeTables(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + RECIPE_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, instructions TEXT NOT NULL)");
        database.execSQL("CREATE TABLE " + RECIPE_INGREDIENT_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_id INTEGER NOT NULL, name TEXT NOT NULL, "
                + "quantity REAL NOT NULL, unit TEXT NOT NULL, "
                + "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
    }

    private void createSettingsTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE settings ("
                + "id INTEGER PRIMARY KEY, expiring_alerts INTEGER NOT NULL, "
                + "preferred_unit TEXT NOT NULL)");
        database.execSQL("INSERT INTO settings VALUES (1, 1, 'Metric')");
    }

    private void seedRecipes(SQLiteDatabase database) {
        seedBreakfastRecipes(database);
        seedLunchRecipes(database);
        seedDinnerRecipes(database);
    }

    private void seedBreakfastRecipes(SQLiteDatabase database) {
        long recipeId = addRecipe(database, "Scrambled Eggs", "Whisk the eggs. Cook them with butter until set.");
        addIngredient(database, recipeId, "egg", 2, "each");
        addIngredient(database, recipeId, "butter", 1, "tbsp");

        recipeId = addRecipe(database, "Cheese Omelette", "Cook the beaten eggs, add cheese, then fold the omelette.");
        addIngredient(database, recipeId, "egg", 2, "each");
        addIngredient(database, recipeId, "cheese", 50, "g");

        recipeId = addRecipe(database, "Tomato Omelette", "Cook the tomato briefly, add beaten eggs, and cook until set.");
        addIngredient(database, recipeId, "egg", 2, "each");
        addIngredient(database, recipeId, "tomato", 1, "each");

        recipeId = addRecipe(database, "Banana Oats", "Cook the oats with milk and serve with sliced banana.");
        addIngredient(database, recipeId, "oats", 50, "g");
        addIngredient(database, recipeId, "milk", 250, "ml");
        addIngredient(database, recipeId, "banana", 1, "each");

        recipeId = addRecipe(database, "Pancakes", "Mix all ingredients and cook small pancakes in a warm pan.");
        addIngredient(database, recipeId, "flour", 100, "g");
        addIngredient(database, recipeId, "milk", 200, "ml");
        addIngredient(database, recipeId, "egg", 1, "each");

        recipeId = addRecipe(database, "Yogurt Banana Bowl", "Slice the banana and stir it into the yogurt.");
        addIngredient(database, recipeId, "yogurt", 200, "g");
        addIngredient(database, recipeId, "banana", 1, "each");
    }

    private void seedLunchRecipes(SQLiteDatabase database) {
        long recipeId = addRecipe(database, "Cheese Toast", "Add cheese to the bread and toast until melted.");
        addIngredient(database, recipeId, "bread", 2, "slice");
        addIngredient(database, recipeId, "cheese", 50, "g");

        recipeId = addRecipe(database, "Tomato Toast", "Butter the toast and top it with sliced tomato.");
        addIngredient(database, recipeId, "bread", 2, "slice");
        addIngredient(database, recipeId, "tomato", 1, "each");
        addIngredient(database, recipeId, "butter", 1, "tbsp");

        recipeId = addRecipe(database, "Peanut Butter Toast", "Toast the bread and spread peanut butter over it.");
        addIngredient(database, recipeId, "bread", 2, "slice");
        addIngredient(database, recipeId, "peanut butter", 2, "tbsp");

        recipeId = addRecipe(database, "Tuna Sandwich", "Mix tuna with mayonnaise and place it between the bread slices.");
        addIngredient(database, recipeId, "bread", 2, "slice");
        addIngredient(database, recipeId, "tuna", 100, "g");
        addIngredient(database, recipeId, "mayonnaise", 1, "tbsp");

        recipeId = addRecipe(database, "Bean Toast", "Warm the beans and spoon them over toasted bread.");
        addIngredient(database, recipeId, "bread", 2, "slice");
        addIngredient(database, recipeId, "beans", 150, "g");

        recipeId = addRecipe(database, "Fruit Yogurt", "Chop the fruit and mix it with the yogurt.");
        addIngredient(database, recipeId, "yogurt", 200, "g");
        addIngredient(database, recipeId, "apple", 1, "each");
        addIngredient(database, recipeId, "banana", 1, "each");

        recipeId = addRecipe(database, "Rice and Egg", "Cook the egg and serve it over warm rice.");
        addIngredient(database, recipeId, "rice", 100, "g");
        addIngredient(database, recipeId, "egg", 1, "each");
    }

    private void seedDinnerRecipes(SQLiteDatabase database) {
        long recipeId = addRecipe(database, "Chicken Rice", "Cook the chicken thoroughly and serve it with rice.");
        addIngredient(database, recipeId, "chicken", 200, "g");
        addIngredient(database, recipeId, "rice", 100, "g");

        recipeId = addRecipe(database, "Tomato Pasta", "Cook the pasta and combine it with chopped cooked tomato.");
        addIngredient(database, recipeId, "pasta", 100, "g");
        addIngredient(database, recipeId, "tomato", 2, "each");

        recipeId = addRecipe(database, "Cheese Pasta", "Cook the pasta and stir in warm milk and cheese.");
        addIngredient(database, recipeId, "pasta", 100, "g");
        addIngredient(database, recipeId, "cheese", 50, "g");
        addIngredient(database, recipeId, "milk", 100, "ml");

        recipeId = addRecipe(database, "Potato Egg Hash", "Cook chopped potato until soft, then add and cook the eggs.");
        addIngredient(database, recipeId, "potato", 2, "each");
        addIngredient(database, recipeId, "egg", 2, "each");

        recipeId = addRecipe(database, "Vegetable Rice", "Cook the carrot and peas, then mix them into warm rice.");
        addIngredient(database, recipeId, "rice", 100, "g");
        addIngredient(database, recipeId, "carrot", 1, "each");
        addIngredient(database, recipeId, "peas", 50, "g");

        recipeId = addRecipe(database, "Chicken Potato", "Cook the chicken and potato together until both are tender.");
        addIngredient(database, recipeId, "chicken", 200, "g");
        addIngredient(database, recipeId, "potato", 2, "each");

        recipeId = addRecipe(database, "Tomato Soup", "Simmer chopped tomatoes in water and blend until smooth.");
        addIngredient(database, recipeId, "tomato", 4, "each");
        addIngredient(database, recipeId, "water", 500, "ml");
    }

    private long addRecipe(SQLiteDatabase database, String name, String instructions) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("instructions", instructions);
        return database.insertOrThrow(RECIPE_TABLE, null, values);
    }

    private void addIngredient(SQLiteDatabase database, long recipeId, String name,
                               double quantity, String unit) {
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        database.insertOrThrow(RECIPE_INGREDIENT_TABLE, null, values);
    }
}
