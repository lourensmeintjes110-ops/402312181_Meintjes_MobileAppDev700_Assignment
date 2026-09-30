package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView txtRecipeName;
    private TextView txtRecipeDescription;
    private TextView txtRecipeIngredients;
    private TextView txtRecipeMatch;
    private TextView txtRecipeInstructions;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        // Find views
        txtRecipeName =
                findViewById(R.id.txtRecipeName);

        txtRecipeDescription =
                findViewById(R.id.txtRecipeDescription);

        txtRecipeIngredients =
                findViewById(R.id.txtRecipeIngredients);

        txtRecipeMatch =
                findViewById(R.id.txtRecipeMatch);

        txtRecipeInstructions =
                findViewById(R.id.txtRecipeInstructions);

        // Database
        databaseHelper =
                new DatabaseHelper(this);

        // Get recipe ID
        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        if (recipeId != -1) {
            loadRecipe(recipeId);
        }
    }

    private void loadRecipe(int recipeId) {

        Recipe recipe =
                databaseHelper.getRecipeById(recipeId);

        if (recipe == null) {
            return;
        }

        // Recipe name
        txtRecipeName.setText(
                recipe.getName()
        );

        // Description
        txtRecipeDescription.setText(
                recipe.getDescription()
        );

        // Match percentage
        txtRecipeMatch.setText(
                String.format(
                        "Pantry Match: %.0f%% (%d of %d ingredients)",
                        recipe.getMatchPercentage(),
                        recipe.getMatchedIngredients(),
                        recipe.getTotalIngredients()
                )
        );

        // Instructions
        txtRecipeInstructions.setText(
                recipe.getInstructions()
        );

        // Get recipe ingredients
        String ingredients =
                databaseHelper.getRecipeIngredients(
                        recipeId
                );

        txtRecipeIngredients.setText(
                ingredients
        );
    }
}