package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        databaseHelper =
                new DatabaseHelper(requireContext());

        recyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

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

        ingredientList =
                databaseHelper.getAllIngredients();

        ingredientAdapter =
                new IngredientAdapter(
                        ingredientList,
                        ingredient -> showDeleteDialog(ingredient)
                );

        recyclerViewIngredients.setAdapter(
                ingredientAdapter
        );

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

    private void showDeleteDialog(Ingredient ingredient) {

        new AlertDialog.Builder(requireContext())

                .setTitle("Delete Ingredient")

                .setMessage(
                        "Are you sure you want to delete \"" +
                                ingredient.getName() +
                                "\"?"
                )

                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            int result =
                                    databaseHelper.deleteIngredient(
                                            ingredient.getId()
                                    );

                            if (result > 0) {

                                Toast.makeText(
                                        requireContext(),
                                        "Ingredient deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadIngredients();

                            } else {

                                Toast.makeText(
                                        requireContext(),
                                        "Unable to delete ingredient",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .show();
    }
}