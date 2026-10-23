package com.ruru.smartpantry;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** Adds a new pantry ingredient or edits an existing one. */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_PANTRY_ITEM_ID = "pantry_item_id";

    private PantryDatabaseHelper databaseHelper;
    private EditText nameEditText;
    private EditText quantityEditText;
    private Spinner unitSpinner;
    private EditText expiryDateEditText;
    private long pantryItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        databaseHelper = new PantryDatabaseHelper(this);
        setupViews();

        pantryItemId = getIntent().getLongExtra(EXTRA_PANTRY_ITEM_ID, -1);
        if (pantryItemId != -1) {
            setTitle(R.string.edit_ingredient_title);
            loadPantryItem();
        }
    }

    private void setupViews() {
        nameEditText = findViewById(R.id.nameEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        unitSpinner = findViewById(R.id.unitSpinner);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.ingredient_units, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);
        findViewById(R.id.saveIngredientButton).setOnClickListener(view -> savePantryItem());
    }

    private void loadPantryItem() {
        PantryItem pantryItem = databaseHelper.getPantryItem(pantryItemId);
        if (pantryItem == null) {
            Toast.makeText(this, R.string.ingredient_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        nameEditText.setText(pantryItem.getName());
        quantityEditText.setText(String.valueOf(pantryItem.getQuantity()));
        expiryDateEditText.setText(pantryItem.getExpiryDate());
        selectUnit(pantryItem.getUnit());
    }

    private void selectUnit(String unit) {
        ArrayAdapter<?> unitAdapter = (ArrayAdapter<?>) unitSpinner.getAdapter();
        int unitPosition = unitAdapter.getPosition(unit);
        unitSpinner.setSelection(Math.max(unitPosition, 0));
    }

    private void savePantryItem() {
        String name = nameEditText.getText().toString().trim();
        String quantityText = quantityEditText.getText().toString().trim();
        if (!isValidInput(name, quantityText)) {
            return;
        }

        double quantity = Double.parseDouble(quantityText);
        String unit = unitSpinner.getSelectedItem().toString();
        String expiryDate = expiryDateEditText.getText().toString().trim();
        if (pantryItemId == -1) {
            databaseHelper.addPantryItem(name, quantity, unit, expiryDate);
        } else {
            databaseHelper.updatePantryItem(pantryItemId, name, quantity, unit, expiryDate);
        }
        Toast.makeText(this, R.string.ingredient_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isValidInput(String name, String quantityText) {
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.name_required));
            return false;
        }
        try {
            if (Double.parseDouble(quantityText) <= 0) {
                quantityEditText.setError(getString(R.string.quantity_positive));
                return false;
            }
        } catch (NumberFormatException exception) {
            quantityEditText.setError(getString(R.string.quantity_required));
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
