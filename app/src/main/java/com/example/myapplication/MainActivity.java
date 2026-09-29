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
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ---------------------------------------
        // Database
        // ---------------------------------------
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        // ---------------------------------------
        // System window insets
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

        // Handle navigation with ONE click
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

        // ---------------------------------------
        // Open Pantry when app starts
        // ---------------------------------------
        if (savedInstanceState == null) {

            bottomNavigationView.setSelectedItemId(
                    R.id.nav_pantry
            );
        }
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