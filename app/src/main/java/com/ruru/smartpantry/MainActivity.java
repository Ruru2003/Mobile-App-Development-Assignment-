package com.ruru.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

/** Displays the user's pantry ingredients. */
public class MainActivity extends AppCompatActivity {

    private PantryDatabaseHelper databaseHelper;
    private PantryAdapter pantryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new PantryDatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        setupPantryList();
        findViewById(R.id.addIngredientButton).setOnClickListener(
                view -> openAddIngredientScreen());
    }

    private void setupPantryList() {
        ListView pantryListView = findViewById(R.id.pantryListView);
        TextView emptyPantryTextView = findViewById(R.id.emptyPantryTextView);
        pantryAdapter = new PantryAdapter(this);
        pantryListView.setAdapter(pantryAdapter);
        pantryListView.setEmptyView(emptyPantryTextView);
        pantryListView.setOnItemClickListener((parent, view, position, id) ->
                openEditIngredientScreen(pantryAdapter.getItem(position)));
        pantryListView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmation(pantryAdapter.getItem(position));
            return true;
        });
    }

    private void openAddIngredientScreen() {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        startActivity(intent);
    }

    private void openEditIngredientScreen(PantryItem pantryItem) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_PANTRY_ITEM_ID, pantryItem.getId());
        startActivity(intent);
    }

    private void openSuggestedRecipesScreen() {
        startActivity(new Intent(this, SuggestedRecipesActivity.class));
    }

    private void openSettingsScreen() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void showDeleteConfirmation(PantryItem pantryItem) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_ingredient_title)
                .setMessage(getString(R.string.delete_ingredient_message, pantryItem.getName()))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.deletePantryItem(pantryItem.getId());
                    refreshPantryList();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void refreshPantryList() {
        pantryAdapter.replaceItems(databaseHelper.getAllPantryItems());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPantryList();
    }

    /** Adds the main navigation choices to the toolbar. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /** Opens the screen selected from the toolbar menu. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSuggestedRecipes) {
            openSuggestedRecipesScreen();
            return true;
        }
        if (item.getItemId() == R.id.menuSettings) {
            openSettingsScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
