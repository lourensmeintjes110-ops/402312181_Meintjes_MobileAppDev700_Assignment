package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // =========================================================
    // DATABASE SETTINGS
    // =========================================================

    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 2;

    // =========================================================
    // TABLE NAMES
    // =========================================================

    private static final String TABLE_INGREDIENTS =
            "ingredients";

    private static final String TABLE_RECIPES =
            "recipes";

    private static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // =========================================================
    // DATABASE CREATION
    // =========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON;");


        // =====================================================
        // INGREDIENTS TABLE
        // =====================================================

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_INGREDIENTS +
                        " (" +
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
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_RECIPES +
                        " (" +
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
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_RECIPE_INGREDIENTS +
                        " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "quantity REAL DEFAULT 0, " +
                        "unit TEXT, " +
                        "FOREIGN KEY(recipe_id) " +
                        "REFERENCES recipes(id) " +
                        "ON DELETE CASCADE" +
                        ")"
        );


        // =====================================================
        // ADD SAMPLE RECIPES
        // =====================================================

        addSampleRecipes(db);
    }


    // =========================================================
    // DATABASE OPEN
    // =========================================================

    @Override
    public void onOpen(SQLiteDatabase db) {

        super.onOpen(db);

        if (!db.isReadOnly()) {

            db.execSQL(
                    "PRAGMA foreign_keys=ON;"
            );
        }
    }


    // =========================================================
    // DATABASE UPGRADE
    // =========================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        Log.d(
                "DatabaseHelper",
                "Database upgrade: " +
                        oldVersion +
                        " -> " +
                        newVersion
        );

        /*
         * Version 2 introduced recipe support.
         */
        if (oldVersion < 2) {

            // Create recipes table
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS recipes (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT NOT NULL, " +
                            "description TEXT, " +
                            "instructions TEXT" +
                            ")"
            );

            // Create recipe ingredients table
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            "recipe_ingredients (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "recipe_id INTEGER NOT NULL, " +
                            "ingredient_name TEXT NOT NULL, " +
                            "quantity REAL DEFAULT 0, " +
                            "unit TEXT, " +
                            "FOREIGN KEY(recipe_id) " +
                            "REFERENCES recipes(id) " +
                            "ON DELETE CASCADE" +
                            ")"
            );

            // Add sample recipes
            addSampleRecipes(db);
        }
    }


    // =========================================================
    // ADD INGREDIENT
    // =========================================================

    public long addIngredient(Ingredient ingredient) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                ingredient.getName()
        );

        values.put(
                "quantity",
                ingredient.getQuantity()
        );

        values.put(
                "unit",
                ingredient.getUnit()
        );

        values.put(
                "expiry_date",
                ingredient.getExpiryDate()
        );

        values.put(
                "category",
                ingredient.getCategory()
        );

        return db.insert(
                TABLE_INGREDIENTS,
                null,
                values
        );
    }


    // =========================================================
    // UPDATE INGREDIENT
    // =========================================================

    public int updateIngredient(
            Ingredient ingredient) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                ingredient.getName()
        );

        values.put(
                "quantity",
                ingredient.getQuantity()
        );

        values.put(
                "unit",
                ingredient.getUnit()
        );

        values.put(
                "expiry_date",
                ingredient.getExpiryDate()
        );

        values.put(
                "category",
                ingredient.getCategory()
        );

        return db.update(
                TABLE_INGREDIENTS,
                values,
                "id = ?",
                new String[]{
                        String.valueOf(
                                ingredient.getId()
                        )
                }
        );
    }


    // =========================================================
    // DELETE INGREDIENT
    // =========================================================

    public int deleteIngredient(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        return db.delete(
                TABLE_INGREDIENTS,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }


    // =========================================================
    // GET INGREDIENT BY ID
    // =========================================================

    public Ingredient getIngredientById(int id) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = null;

        Ingredient ingredient = null;

        try {

            cursor = db.rawQuery(
                    "SELECT id, name, quantity, unit, " +
                            "expiry_date, category " +
                            "FROM ingredients " +
                            "WHERE id = ?",
                    new String[]{
                            String.valueOf(id)
                    }
            );

            if (cursor.moveToFirst()) {

                ingredient =
                        new Ingredient();

                ingredient.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        )
                );

                ingredient.setName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "name"
                                )
                        )
                );

                ingredient.setQuantity(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "quantity"
                                )
                        )
                );

                ingredient.setUnit(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "unit"
                                )
                        )
                );

                ingredient.setExpiryDate(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "expiry_date"
                                )
                        )
                );

                ingredient.setCategory(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "category"
                                )
                        )
                );
            }

        } catch (Exception e) {

            Log.e(
                    "DatabaseHelper",
                    "Error getting ingredient",
                    e
            );

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return ingredient;
    }


    // =========================================================
    // GET ALL INGREDIENTS
    // =========================================================

    public ArrayList<Ingredient> getAllIngredients() {

        ArrayList<Ingredient> ingredientList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = db.rawQuery(
                    "SELECT id, name, quantity, unit, " +
                            "expiry_date, category " +
                            "FROM ingredients " +
                            "ORDER BY name COLLATE NOCASE ASC",
                    null
            );

            while (cursor.moveToNext()) {

                Ingredient ingredient =
                        new Ingredient();

                ingredient.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        )
                );

                ingredient.setName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "name"
                                )
                        )
                );

                ingredient.setQuantity(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "quantity"
                                )
                        )
                );

                ingredient.setUnit(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "unit"
                                )
                        )
                );

                ingredient.setExpiryDate(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "expiry_date"
                                )
                        )
                );

                ingredient.setCategory(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "category"
                                )
                        )
                );

                ingredientList.add(
                        ingredient
                );
            }

        } catch (Exception e) {

            Log.e(
                    "DatabaseHelper",
                    "Error getting ingredients",
                    e
            );

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return ingredientList;
    }


    // =========================================================
    // ADD RECIPE
    // =========================================================

    public long addRecipe(
            String name,
            String description,
            String instructions) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                name
        );

        values.put(
                "description",
                description
        );

        values.put(
                "instructions",
                instructions
        );

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }


    // =========================================================
    // ADD RECIPE FROM RECIPE OBJECT
    // =========================================================

    public long addRecipe(
            Recipe recipe) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                recipe.getName()
        );

        values.put(
                "description",
                recipe.getDescription()
        );

        values.put(
                "instructions",
                recipe.getInstructions()
        );

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }


    // =========================================================
    // ADD RECIPE INGREDIENT
    // =========================================================

    public long addRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit) {

        ContentValues values =
                new ContentValues();

        values.put(
                "recipe_id",
                recipeId
        );

        values.put(
                "ingredient_name",
                ingredientName
        );

        values.put(
                "quantity",
                quantity
        );

        values.put(
                "unit",
                unit
        );

        return db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }


    // =========================================================
    // ADD RECIPE INGREDIENT USING DATABASE HELPER
    // =========================================================

    public long addRecipeIngredient(
            int recipeId,
            String ingredientName,
            double quantity,
            String unit) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "recipe_id",
                recipeId
        );

        values.put(
                "ingredient_name",
                ingredientName
        );

        values.put(
                "quantity",
                quantity
        );

        values.put(
                "unit",
                unit
        );

        return db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }


    // =========================================================
    // GET RECIPE BY ID
    // =========================================================

    public Recipe getRecipeById(int id) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = null;

        Recipe recipe = null;

        try {

            cursor = db.rawQuery(
                    "SELECT id, name, description, instructions " +
                            "FROM recipes " +
                            "WHERE id = ?",
                    new String[]{
                            String.valueOf(id)
                    }
            );

            if (cursor.moveToFirst()) {

                recipe =
                        new Recipe();

                recipe.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        "id"
                                )
                        )
                );

                recipe.setName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "name"
                                )
                        )
                );

                recipe.setDescription(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "description"
                                )
                        )
                );

                recipe.setInstructions(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "instructions"
                                )
                        )
                );

                // Calculate match against pantry
                calculateRecipeMatch(
                        db,
                        recipe
                );
            }

        } catch (Exception e) {

            Log.e(
                    "DatabaseHelper",
                    "Error getting recipe: " + id,
                    e
            );

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return recipe;
    }


    // =========================================================
    // CALCULATE RECIPE MATCH
    // =========================================================

    private void calculateRecipeMatch(
            SQLiteDatabase db,
            Recipe recipe) {

        int totalIngredients = 0;

        int matchedIngredients = 0;

        Cursor ingredientCursor = null;

        try {

            ingredientCursor = db.rawQuery(
                    "SELECT ingredient_name, quantity, unit " +
                            "FROM recipe_ingredients " +
                            "WHERE recipe_id = ?",
                    new String[]{
                            String.valueOf(
                                    recipe.getId()
                            )
                    }
            );

            while (
                    ingredientCursor.moveToNext()
            ) {

                String recipeIngredient =
                        ingredientCursor.getString(
                                ingredientCursor
                                        .getColumnIndexOrThrow(
                                                "ingredient_name"
                                        )
                        );

                double requiredQuantity =
                        ingredientCursor.getDouble(
                                ingredientCursor
                                        .getColumnIndexOrThrow(
                                                "quantity"
                                        )
                        );

                String recipeUnit =
                        ingredientCursor.getString(
                                ingredientCursor
                                        .getColumnIndexOrThrow(
                                                "unit"
                                        )
                        );

                if (recipeIngredient == null ||
                        recipeIngredient.trim().isEmpty()) {

                    continue;
                }

                totalIngredients++;

                Cursor pantryCursor = null;

                try {

                    pantryCursor = db.rawQuery(
                            "SELECT quantity, unit " +
                                    "FROM ingredients " +
                                    "WHERE LOWER(TRIM(name)) = " +
                                    "LOWER(TRIM(?)) " +
                                    "LIMIT 1",
                            new String[]{
                                    recipeIngredient.trim()
                            }
                    );

                    if (pantryCursor.moveToFirst()) {

                        double pantryQuantity =
                                pantryCursor.getDouble(
                                        pantryCursor
                                                .getColumnIndexOrThrow(
                                                        "quantity"
                                                )
                                );

                        String pantryUnit =
                                pantryCursor.getString(
                                        pantryCursor
                                                .getColumnIndexOrThrow(
                                                        "unit"
                                                )
                                );

                        if (isQuantityAvailable(
                                pantryQuantity,
                                pantryUnit,
                                requiredQuantity,
                                recipeUnit)) {

                            matchedIngredients++;
                        }
                    }

                } finally {

                    if (pantryCursor != null) {
                        pantryCursor.close();
                    }
                }
            }

        } finally {

            if (ingredientCursor != null) {
                ingredientCursor.close();
            }
        }

        recipe.setTotalIngredients(
                totalIngredients
        );

        recipe.setMatchedIngredients(
                matchedIngredients
        );

        double percentage = 0;

        if (totalIngredients > 0) {

            percentage =
                    ((double) matchedIngredients /
                            totalIngredients) * 100.0;
        }

        recipe.setMatchPercentage(
                percentage
        );
    }


    // =========================================================
    // CHECK QUANTITY
    // =========================================================

    private boolean isQuantityAvailable(
            double pantryQuantity,
            String pantryUnit,
            double requiredQuantity,
            String recipeUnit) {

        // If no quantity is required,
        // only the ingredient needs to exist.
        if (requiredQuantity <= 0) {

            return true;
        }

        String pantryUnitClean =
                pantryUnit == null
                        ? ""
                        : pantryUnit.trim().toLowerCase();

        String recipeUnitClean =
                recipeUnit == null
                        ? ""
                        : recipeUnit.trim().toLowerCase();

        // Cannot compare quantities without units.
        if (pantryUnitClean.isEmpty() ||
                recipeUnitClean.isEmpty()) {

            return false;
        }

        // Units must match.
        if (!pantryUnitClean.equals(
                recipeUnitClean)) {

            return false;
        }

        return pantryQuantity >= requiredQuantity;
    }


    // =========================================================
    // GET SUGGESTED RECIPES
    // =========================================================

    public ArrayList<Recipe> getSuggestedRecipes() {

        ArrayList<Recipe> suggestedRecipes =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor recipeCursor = null;

        try {

            recipeCursor = db.rawQuery(
                    "SELECT id, name, description, instructions " +
                            "FROM recipes " +
                            "ORDER BY name COLLATE NOCASE ASC",
                    null
            );

            while (
                    recipeCursor.moveToNext()
            ) {

                Recipe recipe =
                        new Recipe();

                recipe.setId(
                        recipeCursor.getInt(
                                recipeCursor
                                        .getColumnIndexOrThrow(
                                                "id"
                                        )
                        )
                );

                recipe.setName(
                        recipeCursor.getString(
                                recipeCursor
                                        .getColumnIndexOrThrow(
                                                "name"
                                        )
                        )
                );

                recipe.setDescription(
                        recipeCursor.getString(
                                recipeCursor
                                        .getColumnIndexOrThrow(
                                                "description"
                                        )
                        )
                );

                recipe.setInstructions(
                        recipeCursor.getString(
                                recipeCursor
                                        .getColumnIndexOrThrow(
                                                "instructions"
                                        )
                        )
                );

                // Calculate pantry match
                calculateRecipeMatch(
                        db,
                        recipe
                );

                /*
                 * Only show recipes where:
                 *
                 * 1. The recipe has ingredients.
                 * 2. At least one ingredient is
                 *    available in the pantry.
                 */

                if (recipe.getTotalIngredients() > 0 &&
                        recipe.getMatchedIngredients() > 0) {

                    suggestedRecipes.add(
                            recipe
                    );
                }
            }

        } catch (Exception e) {

            Log.e(
                    "DatabaseHelper",
                    "Error getting suggested recipes",
                    e
            );

        } finally {

            if (recipeCursor != null) {
                recipeCursor.close();
            }
        }


        // =====================================================
        // SORT BY MATCH PERCENTAGE
        // =====================================================

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
    // GET RECIPE INGREDIENTS
    // =========================================================

    public ArrayList<String> getRecipeIngredients(
            int recipeId) {

        ArrayList<String> ingredients =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = db.rawQuery(
                    "SELECT ingredient_name, quantity, unit " +
                            "FROM recipe_ingredients " +
                            "WHERE recipe_id = ? " +
                            "ORDER BY id ASC",
                    new String[]{
                            String.valueOf(recipeId)
                    }
            );

            while (cursor.moveToNext()) {

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "ingredient_name"
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "quantity"
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "unit"
                                )
                        );

                if (name == null ||
                        name.trim().isEmpty()) {

                    continue;
                }

                StringBuilder ingredientText =
                        new StringBuilder();

                ingredientText.append(
                        name.trim()
                );

                if (quantity > 0) {

                    ingredientText.append(
                            " - "
                    );

                    if (quantity == (long) quantity) {

                        ingredientText.append(
                                (long) quantity
                        );

                    } else {

                        ingredientText.append(
                                quantity
                        );
                    }

                    if (unit != null &&
                            !unit.trim().isEmpty()) {

                        ingredientText.append(
                                " "
                        );

                        ingredientText.append(
                                unit.trim()
                        );
                    }

                } else if (unit != null &&
                        !unit.trim().isEmpty()) {

                    ingredientText.append(
                            " - "
                    );

                    ingredientText.append(
                            unit.trim()
                    );
                }

                ingredients.add(
                        ingredientText.toString()
                );
            }

        } catch (Exception e) {

            Log.e(
                    "DatabaseHelper",
                    "Error getting recipe ingredients",
                    e
            );

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return ingredients;
    }


    // =========================================================
    // ADD SAMPLE RECIPES
    // =========================================================

    private void addSampleRecipes(
            SQLiteDatabase db) {

        // Prevent duplicate sample recipes
        Cursor cursor = null;

        try {

            cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM recipes",
                    null
            );

            if (cursor.moveToFirst()) {

                int count =
                        cursor.getInt(0);

                if (count > 0) {

                    return;
                }
            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }


        // =====================================================
        // 1. CHICKEN RICE
        // =====================================================

        long recipeId =
                insertRecipe(
                        db,
                        "Chicken Rice",
                        "A simple chicken and rice meal.",
                        "1. Cook the rice.\n" +
                                "2. Cook the chicken thoroughly.\n" +
                                "3. Add onion and vegetables.\n" +
                                "4. Combine everything and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Chicken",
                500,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Rice",
                2,
                "cup"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Onion",
                1,
                "unit"
        );


        // =====================================================
        // 2. SPAGHETTI BOLOGNESE
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Spaghetti Bolognese",
                        "Classic spaghetti with a rich meat sauce.",
                        "1. Cook spaghetti.\n" +
                                "2. Brown the mince.\n" +
                                "3. Add onion and tomato sauce.\n" +
                                "4. Simmer and serve with spaghetti."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Spaghetti",
                250,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Beef Mince",
                500,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Tomato",
                2,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Onion",
                1,
                "unit"
        );


        // =====================================================
        // 3. CHEESE OMELETTE
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Cheese Omelette",
                        "Quick omelette with cheese and vegetables.",
                        "1. Beat the eggs.\n" +
                                "2. Heat a pan.\n" +
                                "3. Add eggs and cheese.\n" +
                                "4. Fold and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                3,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Cheese",
                50,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Onion",
                0.5,
                "unit"
        );


        // =====================================================
        // 4. FRIED RICE
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Fried Rice",
                        "Easy fried rice using cooked rice and vegetables.",
                        "1. Cook the rice.\n" +
                                "2. Fry the vegetables.\n" +
                                "3. Add rice and egg.\n" +
                                "4. Stir-fry and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Rice",
                2,
                "cup"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Carrot",
                1,
                "unit"
        );


        // =====================================================
        // 5. CHICKEN PASTA
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Chicken Pasta",
                        "Creamy chicken pasta.",
                        "1. Cook pasta.\n" +
                                "2. Cook chicken.\n" +
                                "3. Add cream and seasoning.\n" +
                                "4. Combine with pasta."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Chicken",
                300,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Pasta",
                250,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Cream",
                200,
                "ml"
        );


        // =====================================================
        // 6. TUNA SANDWICH
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Tuna Sandwich",
                        "Quick tuna sandwich.",
                        "1. Drain tuna.\n" +
                                "2. Mix tuna with mayonnaise.\n" +
                                "3. Add lettuce.\n" +
                                "4. Place between bread slices."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Tuna",
                1,
                "can"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Bread",
                2,
                "slice"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Mayonnaise",
                2,
                "tbsp"
        );


        // =====================================================
        // 7. PANCAKES
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Pancakes",
                        "Simple homemade pancakes.",
                        "1. Mix flour and eggs.\n" +
                                "2. Add milk.\n" +
                                "3. Cook pancakes in a pan.\n" +
                                "4. Serve with your preferred topping."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Flour",
                250,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Milk",
                250,
                "ml"
        );


        // =====================================================
        // 8. VEGETABLE STIR FRY
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Vegetable Stir Fry",
                        "Quick mixed vegetable stir fry.",
                        "1. Chop vegetables.\n" +
                                "2. Heat oil.\n" +
                                "3. Stir-fry vegetables.\n" +
                                "4. Add seasoning and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Carrot",
                2,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Onion",
                1,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Pepper",
                1,
                "unit"
        );


        // =====================================================
        // 9. CHICKEN WRAP
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Chicken Wrap",
                        "Chicken and salad wrap.",
                        "1. Cook the chicken.\n" +
                                "2. Warm the wrap.\n" +
                                "3. Add chicken and vegetables.\n" +
                                "4. Roll and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Chicken",
                250,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Wrap",
                2,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Lettuce",
                50,
                "g"
        );


        // =====================================================
        // 10. TOMATO PASTA
        // =====================================================

        recipeId =
                insertRecipe(
                        db,
                        "Tomato Pasta",
                        "Simple pasta with tomato sauce.",
                        "1. Cook pasta.\n" +
                                "2. Prepare tomato sauce.\n" +
                                "3. Add seasoning.\n" +
                                "4. Mix with pasta and serve."
                );

        addRecipeIngredient(
                db,
                recipeId,
                "Pasta",
                250,
                "g"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Tomato",
                3,
                "unit"
        );

        addRecipeIngredient(
                db,
                recipeId,
                "Onion",
                1,
                "unit"
        );
    }


    // =========================================================
    // INSERT RECIPE
    // =========================================================

    private long insertRecipe(
            SQLiteDatabase db,
            String name,
            String description,
            String instructions) {

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                name
        );

        values.put(
                "description",
                description
        );

        values.put(
                "instructions",
                instructions
        );

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }
}