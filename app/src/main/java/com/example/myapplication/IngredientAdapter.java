package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    // =========================
    // DELETE LISTENER
    // =========================
    public interface OnIngredientLongClickListener {
        void onLongClick(Ingredient ingredient);
    }

    private final ArrayList<Ingredient> ingredientList;
    private final OnIngredientLongClickListener longClickListener;

    // =========================
    // CONSTRUCTOR
    // =========================
    public IngredientAdapter(
            ArrayList<Ingredient> ingredientList,
            OnIngredientLongClickListener longClickListener) {

        this.ingredientList = ingredientList;
        this.longClickListener = longClickListener;
    }

    // =========================
    // CREATE VIEW HOLDER
    // =========================
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

    // =========================
    // BIND DATA
    // =========================
    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position) {

        Ingredient ingredient =
                ingredientList.get(position);

        // Ingredient name
        holder.txtIngredientName.setText(
                ingredient.getName()
        );

        // Quantity
        holder.txtQuantity.setText(
                "Quantity: " +
                        ingredient.getQuantity() +
                        " " +
                        ingredient.getUnit()
        );

        // Category
        holder.txtCategory.setText(
                "Category: " +
                        ingredient.getCategory()
        );

        // Expiry date
        holder.txtExpiryDate.setText(
                "Expiry: " +
                        ingredient.getExpiryDate()
        );

        // =========================
        // TAP = EDIT
        // =========================

        holder.itemView.setOnClickListener(v -> {

            Context context = v.getContext();

            Intent intent = new Intent(
                    context,
                    AddEditIngredientActivity.class
            );

            intent.putExtra(
                    "ingredient_id",
                    ingredient.getId()
            );

            context.startActivity(intent);
        });

        // =========================
        // LONG PRESS = DELETE
        // =========================

        holder.itemView.setOnLongClickListener(v -> {

            if (longClickListener != null) {

                longClickListener.onLongClick(
                        ingredient
                );
            }

            return true;
        });
    }

    // =========================
    // ITEM COUNT
    // =========================
    @Override
    public int getItemCount() {

        if (ingredientList == null) {
            return 0;
        }

        return ingredientList.size();
    }

    // =========================
    // VIEW HOLDER
    // =========================
    public static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtIngredientName;
        TextView txtQuantity;
        TextView txtCategory;
        TextView txtExpiryDate;

        public IngredientViewHolder(
                @NonNull View itemView) {

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