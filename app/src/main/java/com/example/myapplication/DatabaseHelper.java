package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createIngredientsTable = "CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "quantity REAL, " +
                "unit TEXT, " +
                "expiry_date TEXT, " +
                "category TEXT" +
                ")";

        String createRecipesTable = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "instructions TEXT" +
                ")";

        String createRecipeIngredientsTable = "CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, " +
                "ingredient_name TEXT, " +
                "quantity REAL, " +
                "unit TEXT, " +
                "FOREIGN KEY (recipe_id) REFERENCES recipes(id)" +
                ")";

        db.execSQL(createIngredientsTable);
        db.execSQL(createRecipesTable);
        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS ingredients");
        onCreate(db);
    }

    // =========================================================
    // INGREDIENT METHODS
    // =========================================================

    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());
        values.put("category", ingredient.getCategory());
        return db.insert("ingredients", null, values);
    }

    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());
        values.put("category", ingredient.getCategory());
        return db.update("ingredients", values, "id = ?", new String[]{String.valueOf(ingredient.getId())});
    }

    public int deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ingredients", "id = ?", new String[]{String.valueOf(id)});
    }

    public Ingredient getIngredientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, name, quantity, unit, expiry_date, category FROM ingredients WHERE id = ?",
                new String[]{String.valueOf(id)}
        );
        Ingredient ingredient = null;
        if (cursor.moveToFirst()) {
            ingredient = new Ingredient();
            ingredient.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            ingredient.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")));
            ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
            ingredient.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow("expiry_date")));
            ingredient.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
        }
        cursor.close();
        return ingredient;
    }

    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, name, quantity, unit, expiry_date, category FROM ingredients",
                null
        );
        while (cursor.moveToNext()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            ingredient.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")));
            ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
            ingredient.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow("expiry_date")));
            ingredient.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
            ingredientList.add(ingredient);
        }
        cursor.close();
        return ingredientList;
    }

    // =========================================================
    // GET SUGGESTED RECIPES
    // =========================================================
    public ArrayList<Recipe> getSuggestedRecipes() {

        ArrayList<Recipe> suggestedRecipes =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        // Get all recipes
        Cursor recipeCursor = db.rawQuery(
                "SELECT id, name, description, instructions " +
                        "FROM recipes",
                null
        );

        while (recipeCursor.moveToNext()) {

            Recipe recipe = new Recipe();

            int recipeId =
                    recipeCursor.getInt(
                            recipeCursor.getColumnIndexOrThrow("id")
                    );

            recipe.setId(recipeId);

            recipe.setName(
                    recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow("name")
                    )
            );

            recipe.setDescription(
                    recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow("description")
                    )
            );

            recipe.setInstructions(
                    recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow("instructions")
                    )
            );

            // -------------------------------------------------
            // Get ingredients required by this recipe
            // -------------------------------------------------
            Cursor ingredientCursor = db.rawQuery(
                    "SELECT ingredient_name " +
                            "FROM recipe_ingredients " +
                            "WHERE recipe_id = ?",
                    new String[]{
                            String.valueOf(recipeId)
                    }
            );

            int totalIngredients = 0;
            int matchedIngredients = 0;

            while (ingredientCursor.moveToNext()) {

                String recipeIngredient =
                        ingredientCursor.getString(
                                ingredientCursor.getColumnIndexOrThrow(
                                        "ingredient_name"
                                )
                        );

                totalIngredients++;

                // -------------------------------------------------
                // Check if ingredient exists in user's pantry
                // -------------------------------------------------
                Cursor pantryCursor = db.rawQuery(
                        "SELECT id " +
                                "FROM ingredients " +
                                "WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))",
                        new String[]{
                                recipeIngredient
                        }
                );

                // Ingredient exists in pantry
                if (pantryCursor.moveToFirst()) {
                    matchedIngredients++;
                }

                pantryCursor.close();
            }

            ingredientCursor.close();

            // -------------------------------------------------
            // Calculate matching percentage
            // -------------------------------------------------
            double matchPercentage = 0;

            if (totalIngredients > 0) {

                matchPercentage =
                        ((double) matchedIngredients /
                                totalIngredients) * 100;
            }

            recipe.setTotalIngredients(
                    totalIngredients
            );

            recipe.setMatchedIngredients(
                    matchedIngredients
            );

            recipe.setMatchPercentage(
                    matchPercentage
            );

            // -------------------------------------------------
            // Only suggest recipes where the user has
            // at least ONE required ingredient.
            // -------------------------------------------------
            if (matchedIngredients > 0) {

                suggestedRecipes.add(recipe);
            }
        }

        recipeCursor.close();

        // -----------------------------------------------------
        // Sort recipes from highest match to lowest match
        // -----------------------------------------------------
        suggestedRecipes.sort(
                (recipe1, recipe2) ->
                        Double.compare(
                                recipe2.getMatchPercentage(),
                                recipe1.getMatchPercentage()
                        )
        );

        return suggestedRecipes;
    }
}
