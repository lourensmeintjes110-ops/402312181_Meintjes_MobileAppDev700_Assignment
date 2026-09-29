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

public class PantryFragment extends Fragment {

    private RecyclerView recyclerViewIngredients;
    private TextView txtEmptyPantry;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;

    private ArrayList<Ingredient> ingredientList;

    public PantryFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_pantry,
                container,
                false
        );

        // Find views
        recyclerViewIngredients =
                view.findViewById(
                        R.id.recyclerViewIngredients
                );

        txtEmptyPantry =
                view.findViewById(
                        R.id.txtEmptyPantry
                );

        Button btnAddIngredient =
                view.findViewById(
                        R.id.btnAddIngredient
                );

        // Database
        databaseHelper =
                new DatabaseHelper(requireContext());

        // RecyclerView setup
        recyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        // Add Ingredient button
        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    requireActivity(),
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        loadIngredients();
    }

    private void loadIngredients() {

        // Get ingredients from SQLite
        ingredientList =
                databaseHelper.getAllIngredients();

        // Create adapter
        ingredientAdapter =
                new IngredientAdapter(ingredientList);

        // Connect adapter to RecyclerView
        recyclerViewIngredients.setAdapter(
                ingredientAdapter
        );

        // Show/hide empty message
        if (ingredientList.isEmpty()) {

            txtEmptyPantry.setVisibility(
                    View.VISIBLE
            );

            recyclerViewIngredients.setVisibility(
                    View.GONE
            );

        } else {

            txtEmptyPantry.setVisibility(
                    View.GONE
            );

            recyclerViewIngredients.setVisibility(
                    View.VISIBLE
            );
        }
    }
}