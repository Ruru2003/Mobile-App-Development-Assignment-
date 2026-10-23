package com.ruru.smartpantry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/** Displays recipes that can be made from the current pantry. */
public class SuggestedRecipesActivity extends AppCompatActivity {

    private PantryDatabaseHelper databaseHelper;
    private final List<String> recipeNames = new ArrayList<>();
    private ArrayAdapter<String> recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle(R.string.suggested_recipes_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new PantryDatabaseHelper(this);
        setupRecipeList();
    }

    private void setupRecipeList() {
        ListView recipeListView = findViewById(R.id.recipeListView);
        TextView emptyRecipesTextView = findViewById(R.id.emptyRecipesTextView);
        recipeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, recipeNames);
        recipeListView.setAdapter(recipeAdapter);
        recipeListView.setEmptyView(emptyRecipesTextView);
    }

    private void loadSuggestedRecipes() {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        List<Recipe> recipes = recipeMatcher.findMakeableRecipes(
                databaseHelper.getAllRecipes(), databaseHelper.getAllPantryItems());
        recipeNames.clear();
        for (Recipe recipe : recipes) {
            recipeNames.add(recipe.getName());
        }
        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

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
