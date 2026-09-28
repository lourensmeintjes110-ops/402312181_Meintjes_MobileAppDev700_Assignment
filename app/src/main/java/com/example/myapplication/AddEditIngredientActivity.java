package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText edtIngredientName;
    private EditText edtQuantity;
    private EditText edtUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        edtIngredientName =
                findViewById(R.id.edtIngredientName);

        edtQuantity =
                findViewById(R.id.edtQuantity);

        edtUnit =
                findViewById(R.id.edtUnit);

        Button btnSave =
                findViewById(R.id.btnSaveIngredient);

        Button btnCancel =
                findViewById(R.id.btnCancelIngredient);

        btnSave.setOnClickListener(v -> {

            String name =
                    edtIngredientName.getText().toString().trim();

            String quantity =
                    edtQuantity.getText().toString().trim();

            String unit =
                    edtUnit.getText().toString().trim();

            // Database functionality will be added later.

            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}