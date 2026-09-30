package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchExpiryWarning;
    private SwitchCompat switchLowStockWarning;

    private SharedPreferences preferences;

    public SettingsFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_settings,
                container,
                false
        );

        switchExpiryWarning =
                view.findViewById(
                        R.id.switchExpiryWarning
                );

        switchLowStockWarning =
                view.findViewById(
                        R.id.switchLowStockWarning
                );

        preferences =
                requireActivity().getSharedPreferences(
                        "SmartPantrySettings",
                        Context.MODE_PRIVATE
                );

        loadSettings();

        switchExpiryWarning.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    preferences.edit()
                            .putBoolean(
                                    "expiry_warning",
                                    isChecked
                            )
                            .apply();
                }
        );

        switchLowStockWarning.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    preferences.edit()
                            .putBoolean(
                                    "low_stock_warning",
                                    isChecked
                            )
                            .apply();
                }
        );

        return view;
    }

    private void loadSettings() {

        boolean expiryWarning =
                preferences.getBoolean(
                        "expiry_warning",
                        true
                );

        boolean lowStockWarning =
                preferences.getBoolean(
                        "low_stock_warning",
                        true
                );

        switchExpiryWarning.setChecked(
                expiryWarning
        );

        switchLowStockWarning.setChecked(
                lowStockWarning
        );
    }
}