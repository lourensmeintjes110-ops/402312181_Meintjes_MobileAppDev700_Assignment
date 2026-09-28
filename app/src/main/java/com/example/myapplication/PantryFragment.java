package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PantryFragment extends Fragment {

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

        Button btnAddIngredient =
                view.findViewById(R.id.btnAddIngredient);

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    requireActivity(),
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        return view;
    }
}