package com.example.bmi;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi.database.sqlite;
import com.example.bmi.database.token;

public class sign_in extends AppCompatActivity {
    EditText editEmail, editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        Button btnSignIn = findViewById(R.id.b2);
        editEmail = findViewById(R.id.editText);
        editPassword = findViewById(R.id.editText1);
        btnSignIn.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(sign_in.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if user exists in the database
            sqlite dbHelper = new sqlite(sign_in.this);
            boolean userExists = dbHelper.checkUser(email, password);
            if (userExists) {
                // When the user signs in, save their email
                token session = new token(sign_in.this);
                session.saveUserEmail(email);

                Toast.makeText(sign_in.this, "Login successful!", Toast.LENGTH_SHORT).show();
                // Proceed to next activity or perform necessary actions
                Intent intent1 = new Intent(sign_in.this, activity_home.class);
                startActivity(intent1);
            } else {
                // User authentication failed
                Toast.makeText(sign_in.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn3 = findViewById(R.id.b3);

        btn3.setOnClickListener(v -> {
            Intent intent = new Intent(this, sign_up.class);
            startActivity(intent);
        });
    }
}