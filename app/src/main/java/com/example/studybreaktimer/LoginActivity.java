package com.example.studybreaktimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvRegister;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connect XML views
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Input validation
            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this,"Please enter username and password",Toast.LENGTH_SHORT).show();
                return;
            }

            // Check user in database
            int userId = db.checkUser(username, password);

            if (userId != -1) {

                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                // Save login session
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putInt("userId", userId);
                editor.putString("username", username);
                editor.apply();

                // Open MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {

                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

            }
        });

        // Open register screen
        tvRegister.setOnClickListener(v -> {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);

        });
    }
}