package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

    private EditText edtSearchIngredient;
    private Spinner spinnerCategory;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;

    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Ingredient> filteredIngredientList;

    private String selectedCategory = "All Categories";
    private String searchText = "";

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

        edtSearchIngredient =
                view.findViewById(
                        R.id.edtSearchIngredient
                );

        spinnerCategory =
                view.findViewById(
                        R.id.spinnerCategory
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

        // ---------------------------------------
        // Add Ingredient
        // ---------------------------------------

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    requireActivity(),
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // ---------------------------------------
        // Category Spinner
        // ---------------------------------------

        setupCategorySpinner();

        // ---------------------------------------
        // Search Listener
        // ---------------------------------------

        edtSearchIngredient.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        searchText =
                                s.toString().trim();

                        filterIngredients();
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );

        // ---------------------------------------
        // Category Listener
        // ---------------------------------------

        spinnerCategory.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        selectedCategory =
                                parent.getItemAtPosition(position)
                                        .toString();

                        filterIngredients();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        loadIngredients();
    }

    // ---------------------------------------
    // Load Ingredients
    // ---------------------------------------

    private void loadIngredients() {

        ingredientList =
                databaseHelper.getAllIngredients();

        if (ingredientList == null) {
            ingredientList =
                    new ArrayList<>();
        }

        filterIngredients();
    }

    // ---------------------------------------
    // Setup Category Spinner
    // ---------------------------------------

    private void setupCategorySpinner() {

        String[] categories = {
                "All Categories",
                "Meat",
                "Dairy",
                "Fruit",
                "Vegetables",
                "Grains",
                "Canned",
                "Frozen",
                "Snacks",
                "Beverages",
                "Other"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(adapter);
    }

    // ---------------------------------------
    // Filter Ingredients
    // ---------------------------------------

    private void filterIngredients() {

        if (ingredientList == null) {
            return;
        }

        filteredIngredientList =
                new ArrayList<>();

        for (Ingredient ingredient :
                ingredientList) {

            boolean matchesSearch = true;
            boolean matchesCategory = true;

            // Search by ingredient name
            if (!searchText.isEmpty()) {

                String ingredientName =
                        ingredient.getName();

                if (ingredientName == null) {
                    ingredientName = "";
                }

                matchesSearch =
                        ingredientName
                                .toLowerCase()
                                .contains(
                                        searchText.toLowerCase()
                                );
            }

            // Filter by category
            if (!selectedCategory.equals(
                    "All Categories")) {

                String ingredientCategory =
                        ingredient.getCategory();

                if (ingredientCategory == null) {
                    ingredientCategory = "";
                }

                matchesCategory =
                        ingredientCategory
                                .equalsIgnoreCase(
                                        selectedCategory
                                );
            }

            if (matchesSearch && matchesCategory) {

                filteredIngredientList.add(
                        ingredient
                );
            }
        }

        updateRecyclerView();
    }

    // ---------------------------------------
    // Update RecyclerView
    // ---------------------------------------

    private void updateRecyclerView() {

        ingredientAdapter =
                new IngredientAdapter(
                        filteredIngredientList,
                        ingredient ->
                                showDeleteDialog(ingredient)
                );

        recyclerViewIngredients.setAdapter(
                ingredientAdapter
        );

        if (filteredIngredientList.isEmpty()) {

            txtEmptyPantry.setText(
                    "No ingredients match your search."
            );

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

    // ---------------------------------------
    // Delete Ingredient
    // ---------------------------------------

    private void showDeleteDialog(
            Ingredient ingredient) {

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