package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipeList;

    public RecipeAdapter(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe = recipeList.get(position);

        holder.txtRecipeName.setText(
                recipe.getName()
        );

        holder.txtRecipeDescription.setText(
                recipe.getDescription()
        );

        holder.txtMatchPercentage.setText(
                String.format(
                        "%.0f%% match",
                        recipe.getMatchPercentage()
                )
        );

        holder.txtIngredientsMatch.setText(
                recipe.getMatchedIngredients() +
                        " of " +
                        recipe.getTotalIngredients() +
                        " ingredients available"
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtRecipeName;
        TextView txtRecipeDescription;
        TextView txtMatchPercentage;
        TextView txtIngredientsMatch;

        public RecipeViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtRecipeName =
                    itemView.findViewById(
                            R.id.txtRecipeName
                    );

            txtRecipeDescription =
                    itemView.findViewById(
                            R.id.txtRecipeDescription
                    );

            txtMatchPercentage =
                    itemView.findViewById(
                            R.id.txtMatchPercentage
                    );

            txtIngredientsMatch =
                    itemView.findViewById(
                            R.id.txtIngredientsMatch
                    );
        }
    }
}