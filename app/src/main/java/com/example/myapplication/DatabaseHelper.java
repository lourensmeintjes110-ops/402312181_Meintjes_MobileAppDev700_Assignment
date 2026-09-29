package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "SmartPantry.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Pantry ingredients table
        db.execSQL(
                "CREATE TABLE ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "expiry_date TEXT, " +
                        "category TEXT" +
                        ")"
        );

        // Recipes table
        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "description TEXT, " +
                        "instructions TEXT" +
                        ")"
        );

        // Recipe ingredients table
        db.execSQL(
                "CREATE TABLE recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "quantity REAL, " +
                        "unit TEXT, " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id)" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS ingredients");

        onCreate(db);
    }

    // =========================
    // ADD INGREDIENT
    // =========================
    public long addIngredient(Ingredient ingredient) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());
        values.put("category", ingredient.getCategory());

        return db.insert(
                "ingredients",
                null,
                values
        );
    }

    // =========================
    // UPDATE INGREDIENT
    // =========================
    public int updateIngredient(Ingredient ingredient) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());
        values.put("category", ingredient.getCategory());

        return db.update(
                "ingredients",
                values,
                "id = ?",
                new String[]{String.valueOf(ingredient.getId())}
        );
    }

    // =========================
    // DELETE INGREDIENT
    // =========================
    public int deleteIngredient(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                "ingredients",
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // =========================
    // GET ALL INGREDIENTS
    // =========================
    public ArrayList<Ingredient> getAllIngredients() {

        ArrayList<Ingredient> ingredientList =
                new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, quantity, unit, expiry_date, category " +
                        "FROM ingredients ORDER BY name ASC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                Ingredient ingredient = new Ingredient();

                ingredient.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        )
                );

                ingredient.setName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        )
                );

                ingredient.setQuantity(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("quantity")
                        )
                );

                ingredient.setUnit(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        )
                );

                ingredient.setExpiryDate(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("expiry_date")
                        )
                );

                ingredient.setCategory(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("category")
                        )
                );

                ingredientList.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredientList;
    }

    // =========================
    // GET INGREDIENT BY ID
    // =========================
    public Ingredient getIngredientById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, quantity, unit, expiry_date, category " +
                        "FROM ingredients WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        Ingredient ingredient = null;

        if (cursor.moveToFirst()) {

            ingredient = new Ingredient();

            ingredient.setId(
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                    )
            );

            ingredient.setName(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    )
            );

            ingredient.setQuantity(
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("quantity")
                    )
            );

            ingredient.setUnit(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("unit")
                    )
            );

            ingredient.setExpiryDate(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("expiry_date")
                    )
            );

            ingredient.setCategory(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("category")
                    )
            );
        }

        cursor.close();

        return ingredient;
    }
}