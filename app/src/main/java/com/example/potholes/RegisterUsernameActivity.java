package com.example.potholes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.potholes.databinding.TextRowItemBinding;

public class RegisterUsernameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_username);
        SharedPreferences sharedPreferences = getSharedPreferences("info",MODE_PRIVATE);

        EditText editText = findViewById(R.id.editTextUsername);

        findViewById(R.id.button).setOnClickListener((v) -> {
            if(editText.getText().toString().equals("")) {
                Toast.makeText(this, "Inserisci un username valido", Toast.LENGTH_SHORT).show();
                editText.getBackground().setTint(Color.RED);
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",editText.getText().toString());
                editor.apply();
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}