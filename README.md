# Ruru Pantry Manager

Ruru Pantry Manager is a beginner-friendly Java Android app for keeping track of food at home and finding recipes that can be made from the available ingredients.

## Main features

- Add, edit, view, and delete pantry ingredients.
- Store a quantity, unit, and optional expiry date for each ingredient.
- Save all pantry data in a local SQLite database.
- Start with 20 built-in recipes stored in SQLite.
- Suggest only recipes for which every required ingredient and quantity is available.
- Handle simple plurals such as `tomato` and `tomatoes`.
- Compare grams with kilograms and millilitres with litres.
- View a recipe's ingredient list and cooking instructions.
- Save expiry-alert and unit-system settings in SQLite.

## Running the app

1. Open this folder in Android Studio.
2. Wait for the Gradle sync to finish.
3. Start an Android emulator.
4. Select the `app` run configuration.
5. Click the green Run button.

The app starts on **My Pantry**. Use **Add Ingredient** to add food. Tap an ingredient to edit it, or press and hold it to delete it. Use the three-dot menu in the top-right corner to open **Suggested Recipes** or **Settings**.

## Easy test example

Add these pantry items:

- `bread`, quantity `2`, unit `slice`
- `cheese`, quantity `50`, unit `g`

Open **Suggested Recipes** from the menu. **Cheese Toast** should appear. Tap it to see its ingredients and instructions.

## Beginner project guide

- `MainActivity.java` displays pantry ingredients.
- `AddEditIngredientActivity.java` adds and edits ingredients.
- `SuggestedRecipesActivity.java` displays matching recipes.
- `RecipeDetailActivity.java` displays one recipe.
- `SettingsActivity.java` saves app settings.
- `PantryDatabaseHelper.java` creates and reads the SQLite database.
- `RecipeMatcher.java` contains the recipe-matching rules.
- `PantryAdapter.java` displays pantry items in the `ListView`.
- `res/layout` contains the XML screen layouts.

## Checks used during development

Run these commands from the project folder:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
.\gradlew.bat lintDebug
```

The recipe matcher has unit tests for complete ingredients, missing ingredients, insufficient quantities, plural names, and kilogram-to-gram conversion.
