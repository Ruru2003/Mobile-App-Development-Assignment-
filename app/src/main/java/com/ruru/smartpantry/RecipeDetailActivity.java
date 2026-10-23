package com.ruru.smartpantry;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

/** Displays the ingredients and cooking instructions for one recipe. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new PantryDatabaseHelper(this);
        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        displayRecipe(databaseHelper.getRecipe(recipeId));
    }

    private void displayRecipe(Recipe recipe) {
        if (recipe == null) {
            Toast.makeText(this, R.string.recipe_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setTitle(recipe.getName());
        TextView ingredientsTextView = findViewById(R.id.ingredientsTextView);
        TextView instructionsTextView = findViewById(R.id.instructionsTextView);
        ingredientsTextView.setText(createIngredientsText(recipe));
        instructionsTextView.setText(recipe.getInstructions());
    }

    private String createIngredientsText(Recipe recipe) {
        NumberFormat quantityFormat = NumberFormat.getNumberInstance();
        quantityFormat.setMaximumFractionDigits(2);
        StringBuilder ingredientsText = new StringBuilder();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            ingredientsText.append("• ")
                    .append(ingredient.getName())
                    .append(": ")
                    .append(quantityFormat.format(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }
        return ingredientsText.toString().trim();
    }

    /** Closes the recipe details when the toolbar back button is pressed. */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
