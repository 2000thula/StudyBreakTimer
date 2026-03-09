package com.example.studybreaktimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnStudy, btnBreak, btnHistory;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get logged-in user
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);

        if(userId == -1){
            Toast.makeText(this,"Please login first",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        btnStudy = findViewById(R.id.btnStudy);
        btnBreak = findViewById(R.id.btnBreak);
        btnHistory = findViewById(R.id.btnHistory);

        // Study Timer
        btnStudy.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            intent.putExtra("mode","study");
            intent.putExtra("userId", userId);

            startActivity(intent);

        });

        // Break Timer
        btnBreak.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            intent.putExtra("mode","break");
            intent.putExtra("userId", userId);

            startActivity(intent);

        });

        // History
        btnHistory.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("userId", userId);

            startActivity(intent);

        });
    }
}