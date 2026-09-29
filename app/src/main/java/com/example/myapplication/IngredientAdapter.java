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

    public interface OnIngredientLongClickListener {
        void onLongClick(Ingredient ingredient);
    }

    private ArrayList<Ingredient> ingredientList;
    private OnIngredientLongClickListener longClickListener;

    public IngredientAdapter(
            ArrayList<Ingredient> ingredientList,
            OnIngredientLongClickListener longClickListener) {

        this.ingredientList = ingredientList;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position) {

        Ingredient ingredient = ingredientList.get(position);

        holder.txtIngredientName.setText(
                ingredient.getName()
        );

        holder.txtQuantity.setText(
                "Quantity: " +
                        ingredient.getQuantity() +
                        " " +
                        ingredient.getUnit()
        );

        holder.txtCategory.setText(
                "Category: " +
                        ingredient.getCategory()
        );

        holder.txtExpiryDate.setText(
                "Expiry: " +
                        ingredient.getExpiryDate()
        );

        // Tap ingredient = Edit
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

        // Long press = Delete
        holder.itemView.setOnLongClickListener(v -> {

            if (longClickListener != null) {
                longClickListener.onLongClick(ingredient);
            }

            return true;
        });
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