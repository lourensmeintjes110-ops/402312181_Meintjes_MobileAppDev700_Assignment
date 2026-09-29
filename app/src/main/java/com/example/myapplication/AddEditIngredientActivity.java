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

    // ID of ingredient being edited
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        edtIngredientName = findViewById(R.id.edtIngredientName);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtUnit = findViewById(R.id.edtUnit);
        edtExpiryDate = findViewById(R.id.edtExpiryDate);
        edtCategory = findViewById(R.id.edtCategory);

        Button btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        Button btnCancelIngredient =
                findViewById(R.id.btnCancelIngredient);

        databaseHelper = new DatabaseHelper(this);

        // Check if we are editing an existing ingredient
        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        if (ingredientId != -1) {

            loadIngredientForEditing();

            btnSaveIngredient.setText("Update Ingredient");

        } else {

            btnSaveIngredient.setText("Save Ingredient");
        }

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());

        btnCancelIngredient.setOnClickListener(v -> finish());
    }

    private void loadIngredientForEditing() {

        Ingredient ingredient =
                databaseHelper.getIngredientById(ingredientId);

        if (ingredient != null) {

            edtIngredientName.setText(
                    ingredient.getName()
            );

            edtQuantity.setText(
                    String.valueOf(ingredient.getQuantity())
            );

            edtUnit.setText(
                    ingredient.getUnit()
            );

            edtExpiryDate.setText(
                    ingredient.getExpiryDate()
            );

            edtCategory.setText(
                    ingredient.getCategory()
            );
        }
    }

    private void saveIngredient() {

        String name =
                edtIngredientName.getText().toString().trim();

        String quantityText =
                edtQuantity.getText().toString().trim();

        String unit =
                edtUnit.getText().toString().trim();

        String expiryDate =
                edtExpiryDate.getText().toString().trim();

        String category =
                edtCategory.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {

            edtIngredientName.setError(
                    "Enter an ingredient name"
            );

            edtIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {

            edtQuantity.setError(
                    "Enter a quantity"
            );

            edtQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {

            edtUnit.setError(
                    "Enter a unit"
            );

            edtUnit.requestFocus();
            return;
        }

        double quantity;

        try {

            quantity = Double.parseDouble(
                    quantityText.replace(",", ".")
            );

        } catch (NumberFormatException e) {

            edtQuantity.setError(
                    "Enter a valid number"
            );

            edtQuantity.requestFocus();
            return;
        }

        Ingredient ingredient = new Ingredient(
                name,
                quantity,
                unit,
                expiryDate,
                category
        );

        // EDIT existing ingredient
        if (ingredientId != -1) {

            ingredient.setId(ingredientId);

            int result =
                    databaseHelper.updateIngredient(ingredient);

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Error updating ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        }

        // ADD new ingredient
        else {

            long result =
                    databaseHelper.addIngredient(ingredient);

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient saved successfully",
                        Toast.LENGTH_SHORT
                ).show();

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
}