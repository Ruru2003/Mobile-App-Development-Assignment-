package com.ruru.smartpantry;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/** Displays the user's pantry ingredients. */
public class MainActivity extends AppCompatActivity {

    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new PantryDatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        findViewById(R.id.addIngredientButton).setOnClickListener(
                view -> openAddIngredientScreen());
    }

    private void openAddIngredientScreen() {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
