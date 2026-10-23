package com.ruru.smartpantry;

import android.content.Intent;
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
    private final List<Recipe> suggestedRecipes = new ArrayList<>();
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
        recipeListView.setOnItemClickListener((parent, view, position, id) ->
                openRecipeDetails(suggestedRecipes.get(position)));
    }

    private void loadSuggestedRecipes() {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        List<Recipe> matchingRecipes = recipeMatcher.findMakeableRecipes(
                databaseHelper.getAllRecipes(), databaseHelper.getAllPantryItems());
        suggestedRecipes.clear();
        suggestedRecipes.addAll(matchingRecipes);
        recipeNames.clear();
        for (Recipe recipe : suggestedRecipes) {
            recipeNames.add(recipe.getName());
        }
        recipeAdapter.notifyDataSetChanged();
    }

    private void openRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
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
