package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText edtIngredientName;
    private EditText edtQuantity;
    private EditText edtUnit;
    private EditText edtExpiryDate;
    private EditText edtCategory;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        // Connect XML fields
        edtIngredientName =
                findViewById(R.id.edtIngredientName);

        edtQuantity =
                findViewById(R.id.edtQuantity);

        edtUnit =
                findViewById(R.id.edtUnit);

        edtExpiryDate =
                findViewById(R.id.edtExpiryDate);

        edtCategory =
                findViewById(R.id.edtCategory);

        Button btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        Button btnCancelIngredient =
                findViewById(R.id.btnCancelIngredient);

        // Open database
        databaseHelper =
                new DatabaseHelper(this);

        // Save button
        btnSaveIngredient.setOnClickListener(v -> {

            saveIngredient();

        });

        // Cancel button
        btnCancelIngredient.setOnClickListener(v -> {

            finish();

        });
    }

    private void saveIngredient() {

        // Get values from form
        String name =
                edtIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                edtQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                edtUnit
                        .getText()
                        .toString()
                        .trim();

        String expiryDate =
                edtExpiryDate
                        .getText()
                        .toString()
                        .trim();

        String category =
                edtCategory
                        .getText()
                        .toString()
                        .trim();

        // Validate name
        if (name.isEmpty()) {

            edtIngredientName.setError(
                    "Enter an ingredient name"
            );

            edtIngredientName.requestFocus();

            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            edtQuantity.setError(
                    "Enter a quantity"
            );

            edtQuantity.requestFocus();

            return;
        }

        // Validate unit
        if (unit.isEmpty()) {

            edtUnit.setError(
                    "Enter a unit"
            );

            edtUnit.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity = Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            edtQuantity.setError(
                    "Enter a valid number"
            );

            edtQuantity.requestFocus();

            return;
        }

        // Create Ingredient object
        Ingredient ingredient =
                new Ingredient(
                        name,
                        quantity,
                        unit,
                        expiryDate,
                        category
                );

        // Insert into SQLite database
        long result =
                databaseHelper.addIngredient(ingredient);

        if (result != -1) {

            Toast.makeText(
                    this,
                    "Ingredient saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            // Close Add Ingredient screen
            finish();

        } else {

            Toast.makeText(
                    this,
                    "Error saving ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}