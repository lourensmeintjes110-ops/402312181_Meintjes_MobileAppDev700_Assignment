package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredientList;

    public IngredientAdapter(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_ingredient,
                        parent,
                        false
                );

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position) {

        Ingredient ingredient =
                ingredientList.get(position);

        holder.txtIngredientName.setText(
                ingredient.getName()
        );

        holder.txtQuantity.setText(
                "Quantity: "
                        + ingredient.getQuantity()
                        + " "
                        + ingredient.getUnit()
        );

        holder.txtCategory.setText(
                "Category: "
                        + ingredient.getCategory()
        );

        holder.txtExpiryDate.setText(
                "Expiry: "
                        + ingredient.getExpiryDate()
        );
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtIngredientName;
        TextView txtQuantity;
        TextView txtCategory;
        TextView txtExpiryDate;

        public IngredientViewHolder(@NonNull View itemView) {

            super(itemView);

            txtIngredientName =
                    itemView.findViewById(
                            R.id.txtIngredientName
                    );

            txtQuantity =
                    itemView.findViewById(
                            R.id.txtQuantity
                    );

            txtCategory =
                    itemView.findViewById(
                            R.id.txtCategory
                    );

            txtExpiryDate =
                    itemView.findViewById(
                            R.id.txtExpiryDate
                    );
        }
    }
}