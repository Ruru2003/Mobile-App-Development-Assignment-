package com.ruru.smartpantry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

/** Lets the user change the app's saved preferences. */
public class SettingsActivity extends AppCompatActivity {

    private PantryDatabaseHelper databaseHelper;
    private SwitchMaterial expiryAlertsSwitch;
    private Spinner unitSystemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new PantryDatabaseHelper(this);
        expiryAlertsSwitch = findViewById(R.id.expiryAlertsSwitch);
        unitSystemSpinner = findViewById(R.id.unitSystemSpinner);
        setupUnitSystemSpinner();
        loadSettings();
        Button saveSettingsButton = findViewById(R.id.saveSettingsButton);
        saveSettingsButton.setOnClickListener(view -> saveSettings());
    }

    private void setupUnitSystemSpinner() {
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.unit_systems, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSystemSpinner.setAdapter(unitAdapter);
    }

    private void loadSettings() {
        expiryAlertsSwitch.setChecked(databaseHelper.areExpiryAlertsEnabled());
        String preferredUnitSystem = databaseHelper.getPreferredUnitSystem();
        int position = preferredUnitSystem.equals("Imperial") ? 1 : 0;
        unitSystemSpinner.setSelection(position);
    }

    private void saveSettings() {
        String preferredUnitSystem = unitSystemSpinner.getSelectedItem().toString();
        databaseHelper.updateSettings(
                expiryAlertsSwitch.isChecked(), preferredUnitSystem);
        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
    }

    /** Closes the settings screen when the toolbar back button is pressed. */
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
