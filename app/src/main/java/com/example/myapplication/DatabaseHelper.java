package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON;");

        // ---------------------------------------------------------
        // INGREDIENTS TABLE
        // ---------------------------------------------------------
        String createIngredientsTable =
                "CREATE TABLE IF NOT EXISTS ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL DEFAULT 0, " +
                        "unit TEXT, " +
                        "expiry_date TEXT, " +
                        "category TEXT" +
                        ")";

        // ---------------------------------------------------------
        // RECIPES TABLE
        // ---------------------------------------------------------
        String createRecipesTable =
                "CREATE TABLE IF NOT EXISTS recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "description TEXT, " +
                        "instructions TEXT" +
                        ")";

        // ---------------------------------------------------------
        // RECIPE INGREDIENTS TABLE
        // ---------------------------------------------------------
        String createRecipeIngredientsTable =
                "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "quantity REAL DEFAULT 0, " +
                        "unit TEXT, " +
                        "FOREIGN KEY (recipe_id) REFERENCES recipes(id) " +
                        "ON DELETE CASCADE" +
                        ")";

        db.execSQL(createIngredientsTable);
        db.execSQL(createRecipesTable);
        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {

            // Enable foreign key support
            db.execSQL("PRAGMA foreign_keys=ON;");

            // Make sure required database structures exist
            ensureDatabaseStructure(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(
                "DatabaseHelper",
                "Upgrading database from version " +
                        oldVersion +
                        " to " +
                        newVersion
        );

        // Make sure all required tables and columns exist.
        ensureDatabaseStructure(db);
    }

    private void ensureDatabaseStructure(SQLiteDatabase db) {

        try {

            // =====================================================
            // INGREDIENTS TABLE
            // =====================================================

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS ingredients (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT NOT NULL, " +
                            "quantity REAL DEFAULT 0, " +
                            "unit TEXT, " +
                            "expiry_date TEXT, " +
                            "category TEXT" +
                            ")"
            );


            // =====================================================
            // RECIPES TABLE
            // =====================================================

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS recipes (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT NOT NULL, " +
                            "description TEXT, " +
                            "instructions TEXT" +
                            ")"
            );


            // =====================================================
            // RECIPE INGREDIENTS TABLE
            // =====================================================

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "recipe_id INTEGER NOT NULL, " +
                            "ingredient_name TEXT NOT NULL, " +
                            "quantity REAL DEFAULT 0, " +
                            "unit TEXT, " +
                            "FOREIGN KEY (recipe_id) REFERENCES recipes(id) " +
                            "ON DELETE CASCADE" +
                            ")"
            );


            // =====================================================
            // CHECK RECIPE INGREDIENTS COLUMNS
            // =====================================================

            boolean hasQuantity = false;
            boolean hasUnit = false;
            boolean hasIngredientName = false;
            boolean hasRecipeId = false;

            try (Cursor cursor =
                         db.rawQuery(
                                 "PRAGMA table_info(recipe_ingredients)",
                                 null
                         )) {

                int nameIndex = cursor.getColumnIndex("name");

                while (cursor.moveToNext()) {

                    if (nameIndex == -1) {
                        continue;
                    }

                    String columnName =
                            cursor.getString(nameIndex);

                    if ("quantity".equalsIgnoreCase(columnName)) {
                        hasQuantity = true;
                    }

                    if ("unit".equalsIgnoreCase(columnName)) {
                        hasUnit = true;
                    }

                    if ("ingredient_name".equalsIgnoreCase(columnName)) {
                        hasIngredientName = true;
                    }

                    if ("recipe_id".equalsIgnoreCase(columnName)) {
                        hasRecipeId = true;
                    }
                }
            }


            // =====================================================
            // ADD MISSING COLUMNS
            // =====================================================

            if (!hasQuantity) {

                db.execSQL(
                        "ALTER TABLE recipe_ingredients " +
                                "ADD COLUMN quantity REAL DEFAULT 0"
                );
            }

            if (!hasUnit) {

                db.execSQL(
                        "ALTER TABLE recipe_ingredients " +
                                "ADD COLUMN unit TEXT"
                );
            }

            if (!hasIngredientName) {

                db.execSQL(
                        "ALTER TABLE recipe_ingredients " +
                                "ADD COLUMN ingredient_name TEXT"
                );
            }

            if (!hasRecipeId) {

                db.execSQL(
                        "ALTER TABLE recipe_ingredients " +
                                "ADD COLUMN recipe_id INTEGER"
                );
            }

        } catch (Exception e) {

            Log.e(
                    "DatabaseHelper",
                    "Error ensuring database structure",
                    e
            );
        }
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
    // CHECK IF PANTRY QUANTITY IS SUFFICIENT
    // =========================================================
    private boolean isQuantityAvailable(
            double pantryQuantity,
            String pantryUnit,
            double requiredQuantity,
            String recipeUnit) {

        // If no quantity was specified in the recipe,
        // only check that the ingredient exists.
        if (requiredQuantity <= 0) {
            return true;
        }

        // Clean units
        String pantryUnitClean =
                pantryUnit == null
                        ? ""
                        : pantryUnit.trim().toLowerCase();

        String recipeUnitClean =
                recipeUnit == null
                        ? ""
                        : recipeUnit.trim().toLowerCase();

        // -----------------------------------------------------
        // Units must match for quantity comparison
        // -----------------------------------------------------
        if (!pantryUnitClean.equals(recipeUnitClean)) {

            // Different units cannot safely be compared.
            // Ingredient existence still counts as available.
            return true;
        }

        // -----------------------------------------------------
        // Pantry must have enough quantity
        // -----------------------------------------------------
        return pantryQuantity >= requiredQuantity;
    }

    // =========================================================
    // GET SUGGESTED RECIPES
    // =========================================================
    public ArrayList<Recipe> getSuggestedRecipes() {

        ArrayList<Recipe> suggestedRecipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Get all recipes
        Cursor recipeCursor = db.rawQuery(
                "SELECT id, name, description, instructions " +
                        "FROM recipes",
                null
        );

        while (recipeCursor.moveToNext()) {

            Recipe recipe = new Recipe();

            int recipeId = recipeCursor.getInt(
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
                    "SELECT ingredient_name, quantity, unit " +
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

                double requiredQuantity =
                        ingredientCursor.getDouble(
                                ingredientCursor.getColumnIndexOrThrow(
                                        "quantity"
                                )
                        );

                String recipeUnit =
                        ingredientCursor.getString(
                                ingredientCursor.getColumnIndexOrThrow(
                                        "unit"
                                )
                        );

                totalIngredients++;

                // -------------------------------------------------
                // Find matching ingredient in pantry
                // -------------------------------------------------
                Cursor pantryCursor = db.rawQuery(
                        "SELECT quantity, unit " +
                                "FROM ingredients " +
                                "WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))",
                        new String[]{
                                recipeIngredient
                        }
                );

                boolean ingredientAvailable = false;

                if (pantryCursor.moveToFirst()) {

                    double pantryQuantity =
                            pantryCursor.getDouble(
                                    pantryCursor.getColumnIndexOrThrow(
                                            "quantity"
                                    )
                            );

                    String pantryUnit =
                            pantryCursor.getString(
                                    pantryCursor.getColumnIndexOrThrow(
                                            "unit"
                                    )
                            );

                    // -------------------------------------------------
                    // Check quantity and unit
                    // -------------------------------------------------
                    if (isQuantityAvailable(
                            pantryQuantity,
                            pantryUnit,
                            requiredQuantity,
                            recipeUnit)) {

                        ingredientAvailable = true;
                    }
                }

                pantryCursor.close();

                // Ingredient exists and sufficient quantity is available
                if (ingredientAvailable) {
                    matchedIngredients++;
                }
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

            recipe.setTotalIngredients(totalIngredients);

            recipe.setMatchedIngredients(matchedIngredients);

            recipe.setMatchPercentage(matchPercentage);

            // -------------------------------------------------
            // Only show recipes with at least one
            // available ingredient
            // -------------------------------------------------
            if (matchedIngredients > 0) {

                suggestedRecipes.add(recipe);
            }
        }

        recipeCursor.close();

        // -----------------------------------------------------
        // Sort highest match percentage first
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

    // =========================================================
    // RECIPE METHODS
    // =========================================================

    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, name, description, instructions FROM recipes WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {
            recipe = new Recipe();
            recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            recipe.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow("instructions")));

            // Calculate matching ingredients from pantry
            Cursor ingredientCursor = db.rawQuery(
                    "SELECT ingredient_name, quantity, unit FROM recipe_ingredients WHERE recipe_id = ?",
                    new String[]{String.valueOf(id)}
            );

            int totalIngredients = 0;
            int matchedIngredients = 0;

            while (ingredientCursor.moveToNext()) {
                String recipeIngredient = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow("ingredient_name")
                );
                double requiredQuantity = ingredientCursor.getDouble(
                        ingredientCursor.getColumnIndexOrThrow("quantity")
                );
                String recipeUnit = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow("unit")
                );

                totalIngredients++;

                Cursor pantryCursor = db.rawQuery(
                        "SELECT quantity, unit FROM ingredients WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))",
                        new String[]{recipeIngredient}
                );

                if (pantryCursor.moveToFirst()) {
                    double pantryQuantity = pantryCursor.getDouble(
                            pantryCursor.getColumnIndexOrThrow("quantity")
                    );
                    String pantryUnit = pantryCursor.getString(
                            pantryCursor.getColumnIndexOrThrow("unit")
                    );

                    if (isQuantityAvailable(pantryQuantity, pantryUnit, requiredQuantity, recipeUnit)) {
                        matchedIngredients++;
                    }
                }
                pantryCursor.close();
            }
            ingredientCursor.close();

            recipe.setTotalIngredients(totalIngredients);
            recipe.setMatchedIngredients(matchedIngredients);

            if (totalIngredients > 0) {
                recipe.setMatchPercentage(((double) matchedIngredients / totalIngredients) * 100);
            }
        }

        cursor.close();
        return recipe;
    }

    public String getRecipeIngredients(int recipeId) {
        StringBuilder builder = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ingredient_name, quantity, unit FROM recipe_ingredients WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("ingredient_name"));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));

            builder.append("• ").append(name);
            if (quantity > 0) {
                builder.append(" - ");
                if (quantity == (long) quantity) {
                    builder.append((long) quantity);
                } else {
                    builder.append(quantity);
                }
                if (unit != null && !unit.trim().isEmpty()) {
                    builder.append(" ").append(unit.trim());
                }
            } else if (unit != null && !unit.trim().isEmpty()) {
                builder.append(" - ").append(unit.trim());
            }
            builder.append("\n");
        }

        cursor.close();
        return builder.toString().trim();
    }
}