package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView txtRecipeName;
    private TextView txtRecipeDescription;
    private TextView txtRecipeIngredients;
    private TextView txtRecipeInstructions;

    private Button btnBack;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        // =========================
        // FIND VIEWS
        // =========================

        txtRecipeName =
                findViewById(R.id.txtRecipeName);

        txtRecipeDescription =
                findViewById(R.id.txtRecipeDescription);

        txtRecipeIngredients =
                findViewById(R.id.txtRecipeIngredients);

        txtRecipeInstructions =
                findViewById(R.id.txtRecipeInstructions);

        btnBack =
                findViewById(R.id.btnBack);

        // =========================
        // DATABASE
        // =========================

        databaseHelper =
                new DatabaseHelper(this);

        // =========================
        // BACK BUTTON
        // =========================

        btnBack.setOnClickListener(v -> finish());

        // =========================
        // GET RECIPE ID
        // =========================

        int recipeId =
                getIntent().getIntExtra("RECIPE_ID", -1);

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Recipe could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // =========================
        // LOAD RECIPE
        // =========================

        loadRecipe(recipeId);
    }

    private void loadRecipe(int recipeId) {

        Recipe recipe =
                databaseHelper.getRecipeById(recipeId);

        if (recipe == null) {

            Toast.makeText(
                    this,
                    "Recipe could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // =========================
        // DISPLAY RECIPE
        // =========================

        txtRecipeName.setText(
                recipe.getName()
        );

        txtRecipeDescription.setText(
                recipe.getDescription()
        );

        txtRecipeInstructions.setText(
                recipe.getInstructions()
        );

        // =========================
        // LOAD INGREDIENTS
        // =========================

        ArrayList<String> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredientText =
                new StringBuilder();

        for (String ingredient : ingredients) {

            ingredientText
                    .append("• ")
                    .append(ingredient)
                    .append("\n");
        }

        if (ingredientText.length() == 0) {

            txtRecipeIngredients.setText(
                    "No ingredients listed."
            );

        } else {

            txtRecipeIngredients.setText(
                    ingredientText.toString()
            );
        }
    }
}
