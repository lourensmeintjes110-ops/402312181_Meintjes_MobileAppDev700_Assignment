package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;

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
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_recipes,
                container,
                false
        );

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
                view.findViewById(R.id.btnAddRecipe);

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
                new DatabaseHelper(requireContext());

        // =========================
        // RECYCLER VIEW
        // =========================
        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        loadSuggestedRecipes();
    }

    // =========================
    // LOAD SUGGESTED RECIPES
    // =========================
    private void loadSuggestedRecipes() {

        recipeList =
                databaseHelper.getSuggestedRecipes();

        RecipeAdapter adapter =
                new RecipeAdapter(recipeList);

        recyclerViewRecipes.setAdapter(adapter);

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