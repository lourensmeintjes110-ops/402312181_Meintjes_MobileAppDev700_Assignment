package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipesFragment extends Fragment {

    private RecyclerView recyclerViewRecipes;
    private TextView txtNoRecipes;

    private DatabaseHelper databaseHelper;

    private ArrayList<Recipe> recipeList;

    public RecipesFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // Load Recipes screen
        View view = inflater.inflate(
                R.layout.fragment_recipes,
                container,
                false
        );

        // =========================
        // FIND VIEWS
        // =========================

        recyclerViewRecipes =
                view.findViewById(
                        R.id.recyclerViewRecipes
                );

        txtNoRecipes =
                view.findViewById(
                        R.id.txtNoRecipes
                );

        // =========================
        // ADD RECIPE BUTTON
        // =========================

        Button btnAddRecipe =
                view.findViewById(
                        R.id.btnAddRecipe
                );

        btnAddRecipe.setOnClickListener(v -> {

            Intent intent = new Intent(
                    requireActivity(),
                    AddRecipeActivity.class
            );

            startActivity(intent);
        });

        // =========================
        // DATABASE
        // =========================

        databaseHelper =
                new DatabaseHelper(
                        requireContext()
                );

        // =========================
        // RECYCLER VIEW
        // =========================

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        return view;
    }

    // =========================
    // SCREEN RESUMES
    // =========================

    @Override
    public void onResume() {

        super.onResume();

        loadSuggestedRecipes();
    }

    // =========================
    // LOAD SUGGESTED RECIPES
    // =========================

    private void loadSuggestedRecipes() {

        // Get suggested recipes from database
        recipeList =
                databaseHelper.getSuggestedRecipes();

        // Make sure the list is not null
        if (recipeList == null) {

            recipeList =
                    new ArrayList<>();
        }

        // =========================
        // CREATE RECIPE ADAPTER
        // =========================

        RecipeAdapter adapter =
                new RecipeAdapter(
                        recipeList,
                        new RecipeAdapter.OnRecipeClickListener() {

                            @Override
                            public void onRecipeClick(
                                    Recipe recipe) {

                                // =========================
                                // OPEN RECIPE DETAIL
                                // =========================

                                Intent intent =
                                        new Intent(
                                                requireActivity(),
                                                RecipeDetailActivity.class
                                        );

                                // Pass recipe ID
                                intent.putExtra(
                                        "RECIPE_ID",
                                        recipe.getId()
                                );

                                startActivity(intent);
                            }
                        }
                );

        // Attach adapter
        recyclerViewRecipes.setAdapter(
                adapter
        );

        // =========================
        // CHECK IF RECIPES EXIST
        // =========================

        if (recipeList.isEmpty()) {

            txtNoRecipes.setVisibility(
                    View.VISIBLE
            );

            recyclerViewRecipes.setVisibility(
                    View.GONE
            );

        } else {

            txtNoRecipes.setVisibility(
                    View.GONE
            );

            recyclerViewRecipes.setVisibility(
                    View.VISIBLE
            );
        }
    }
}