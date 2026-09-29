package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText edtRecipeName;
    private EditText edtRecipeDescription;
    private EditText edtRecipeInstructions;

    private EditText edtIngredientName;
    private EditText edtIngredientQuantity;
    private EditText edtIngredientUnit;

    private TextView txtAddedIngredients;

    private DatabaseHelper databaseHelper;

    // Temporary list of ingredients
    private ArrayList<RecipeIngredient> ingredientList =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_recipe);

        // Recipe fields
        edtRecipeName =
                findViewById(R.id.edtRecipeName);

        edtRecipeDescription =
                findViewById(R.id.edtRecipeDescription);

        edtRecipeInstructions =
                findViewById(R.id.edtRecipeInstructions);

        // Ingredient fields
        edtIngredientName =
                findViewById(R.id.edtIngredientName);

        edtIngredientQuantity =
                findViewById(R.id.edtIngredientQuantity);

        edtIngredientUnit =
                findViewById(R.id.edtIngredientUnit);

        txtAddedIngredients =
                findViewById(R.id.txtAddedIngredients);

        Button btnAddRecipeIngredient =
                findViewById(R.id.btnAddRecipeIngredient);

        Button btnSaveRecipe =
                findViewById(R.id.btnSaveRecipe);

        Button btnCancelRecipe =
                findViewById(R.id.btnCancelRecipe);

        databaseHelper =
                new DatabaseHelper(this);

        // Add ingredient to temporary list
        btnAddRecipeIngredient.setOnClickListener(
                v -> addIngredient()
        );

        // Save complete recipe
        btnSaveRecipe.setOnClickListener(
                v -> saveRecipe()
        );

        // Cancel
        btnCancelRecipe.setOnClickListener(
                v -> finish()
        );
    }

    // =========================================
    // ADD INGREDIENT TO TEMPORARY LIST
    // =========================================
    private void addIngredient() {

        String name =
                edtIngredientName.getText()
                        .toString()
                        .trim();

        String quantityText =
                edtIngredientQuantity.getText()
                        .toString()
                        .trim();

        String unit =
                edtIngredientUnit.getText()
                        .toString()
                        .trim();

        // Validate name
        if (name.isEmpty()) {

            edtIngredientName.setError(
                    "Enter ingredient name"
            );

            edtIngredientName.requestFocus();

            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            edtIngredientQuantity.setError(
                    "Enter quantity"
            );

            edtIngredientQuantity.requestFocus();

            return;
        }

        // Validate unit
        if (unit.isEmpty()) {

            edtIngredientUnit.setError(
                    "Enter unit"
            );

            edtIngredientUnit.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity = Double.parseDouble(
                    quantityText.replace(",", ".")
            );

        } catch (NumberFormatException e) {

            edtIngredientQuantity.setError(
                    "Enter a valid number"
            );

            edtIngredientQuantity.requestFocus();

            return;
        }

        // Create ingredient
        RecipeIngredient ingredient =
                new RecipeIngredient(
                        name,
                        quantity,
                        unit
                );

        // Add to temporary list
        ingredientList.add(ingredient);

        // Refresh displayed ingredients
        updateIngredientDisplay();

        // Clear fields
        edtIngredientName.setText("");
        edtIngredientQuantity.setText("");
        edtIngredientUnit.setText("");

        edtIngredientName.requestFocus();
    }

    // =========================================
    // DISPLAY ADDED INGREDIENTS
    // =========================================
    private void updateIngredientDisplay() {

        if (ingredientList.isEmpty()) {

            txtAddedIngredients.setText(
                    "No ingredients added yet."
            );

            return;
        }

        StringBuilder text =
                new StringBuilder();

        for (int i = 0;
             i < ingredientList.size();
             i++) {

            RecipeIngredient ingredient =
                    ingredientList.get(i);

            text.append(i + 1)
                    .append(". ")
                    .append(ingredient.getName())
                    .append(" - ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        txtAddedIngredients.setText(
                text.toString()
        );
    }

    // =========================================
    // SAVE RECIPE
    // =========================================
    private void saveRecipe() {

        String recipeName =
                edtRecipeName.getText()
                        .toString()
                        .trim();

        String description =
                edtRecipeDescription.getText()
                        .toString()
                        .trim();

        String instructions =
                edtRecipeInstructions.getText()
                        .toString()
                        .trim();

        // Validate recipe name
        if (recipeName.isEmpty()) {

            edtRecipeName.setError(
                    "Enter recipe name"
            );

            edtRecipeName.requestFocus();

            return;
        }

        // Require at least one ingredient
        if (ingredientList.isEmpty()) {

            Toast.makeText(
                    this,
                    "Add at least one ingredient",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        SQLiteDatabase db =
                databaseHelper.getWritableDatabase();

        // Start database transaction
        db.beginTransaction();

        try {

            // =================================
            // INSERT RECIPE
            // =================================

            ContentValues recipeValues =
                    new ContentValues();

            recipeValues.put(
                    "name",
                    recipeName
            );

            recipeValues.put(
                    "description",
                    description
            );

            recipeValues.put(
                    "instructions",
                    instructions
            );

            long recipeId =
                    db.insert(
                            "recipes",
                            null,
                            recipeValues
                    );

            // Check recipe insertion
            if (recipeId == -1) {

                Toast.makeText(
                        this,
                        "Error saving recipe",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // =================================
            // INSERT RECIPE INGREDIENTS
            // =================================

            for (RecipeIngredient ingredient :
                    ingredientList) {

                ContentValues ingredientValues =
                        new ContentValues();

                ingredientValues.put(
                        "recipe_id",
                        recipeId
                );

                ingredientValues.put(
                        "ingredient_name",
                        ingredient.getName()
                );

                ingredientValues.put(
                        "quantity",
                        ingredient.getQuantity()
                );

                ingredientValues.put(
                        "unit",
                        ingredient.getUnit()
                );

                db.insert(
                        "recipe_ingredients",
                        null,
                        ingredientValues
                );
            }

            // Everything succeeded
            db.setTransactionSuccessful();

            Toast.makeText(
                    this,
                    "Recipe saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {

            db.endTransaction();
        }
    }

    // =========================================
    // TEMPORARY RECIPE INGREDIENT CLASS
    // =========================================
    public static class RecipeIngredient {

        private String name;
        private double quantity;
        private String unit;

        public RecipeIngredient(
                String name,
                double quantity,
                String unit) {

            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public double getQuantity() {
            return quantity;
        }

        public String getUnit() {
            return unit;
        }
    }
}
