package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    // Database helper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ---------------------------------------
        // Open/Create SmartPantry.db
        // ---------------------------------------
        databaseHelper = new DatabaseHelper(this);

        // This creates the database if it does not exist
        // and opens it if it already exists.
        databaseHelper.getWritableDatabase();

        // ---------------------------------------
        // Handle system window insets
        // ---------------------------------------
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            0
                    );

                    return insets;
                }
        );

        // ---------------------------------------
        // Bottom Navigation
        // ---------------------------------------
        bottomNavigationView =
                findViewById(R.id.bottomNavigationView);

        // Load Pantry screen when application starts
        if (savedInstanceState == null) {
            loadFragment(new PantryFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_pantry) {

                loadFragment(new PantryFragment());
                return true;

            } else if (itemId == R.id.nav_recipes) {

                loadFragment(new RecipesFragment());
                return true;

            } else if (itemId == R.id.nav_settings) {

                loadFragment(new SettingsFragment());
                return true;
            }

            return false;
        });
    }

    // ---------------------------------------
    // Load Fragment
    // ---------------------------------------
    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        fragment
                )
                .commit();
    }
}
