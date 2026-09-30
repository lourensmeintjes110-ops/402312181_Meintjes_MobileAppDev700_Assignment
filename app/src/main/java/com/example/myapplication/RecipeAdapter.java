package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter
        extends RecyclerView.Adapter<
        RecipeAdapter.RecipeViewHolder> {

    // =========================
    // RECIPE LIST
    // =========================

    private ArrayList<Recipe> recipeList;

    // =========================
    // CLICK LISTENER
    // =========================

    private OnRecipeClickListener onRecipeClickListener;

    // =========================
    // CLICK LISTENER INTERFACE
    // =========================

    public interface OnRecipeClickListener {

        void onRecipeClick(Recipe recipe);
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    public RecipeAdapter(
            ArrayList<Recipe> recipeList,
            OnRecipeClickListener listener) {

        this.recipeList =
                recipeList;

        this.onRecipeClickListener =
                listener;
    }

    // =========================
    // CREATE VIEW HOLDER
    // =========================

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_recipe,
                                parent,
                                false
                        );

        return new RecipeViewHolder(view);
    }

    // =========================
    // BIND DATA
    // =========================

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        // Get recipe
        Recipe recipe =
                recipeList.get(position);

        // =========================
        // RECIPE NAME
        // =========================

        holder.txtRecipeName.setText(
                recipe.getName()
        );

        // =========================
        // DESCRIPTION
        // =========================

        holder.txtRecipeDescription.setText(
                recipe.getDescription()
        );

        // =========================
        // MATCH PERCENTAGE
        // =========================

        holder.txtMatchPercentage.setText(
                String.format(
                        "%.0f%% match",
                        recipe.getMatchPercentage()
                )
        );

        // =========================
        // INGREDIENT MATCH
        // =========================

        holder.txtIngredientsMatch.setText(
                recipe.getMatchedIngredients()
                        + " of "
                        + recipe.getTotalIngredients()
                        + " ingredients available"
        );

        // =========================
        // RECIPE CLICK
        // =========================

        holder.itemView.setOnClickListener(
                v -> {

                    if (onRecipeClickListener != null) {

                        onRecipeClickListener.onRecipeClick(
                                recipe
                        );
                    }
                }
        );
    }

    // =========================
    // ITEM COUNT
    // =========================

    @Override
    public int getItemCount() {

        if (recipeList == null) {

            return 0;
        }

        return recipeList.size();
    }

    // =========================
    // VIEW HOLDER
    // =========================

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtRecipeName;
        TextView txtRecipeDescription;
        TextView txtMatchPercentage;
        TextView txtIngredientsMatch;

        public RecipeViewHolder(
                @NonNull View itemView) {

            super(itemView);

            // Recipe name
            txtRecipeName =
                    itemView.findViewById(
                            R.id.txtRecipeName
                    );

            // Recipe description
            txtRecipeDescription =
                    itemView.findViewById(
                            R.id.txtRecipeDescription
                    );

            // Match percentage
            txtMatchPercentage =
                    itemView.findViewById(
                            R.id.txtMatchPercentage
                    );

            // Ingredients available
            txtIngredientsMatch =
                    itemView.findViewById(
                            R.id.txtIngredientsMatch
                    );
        }
    }
}