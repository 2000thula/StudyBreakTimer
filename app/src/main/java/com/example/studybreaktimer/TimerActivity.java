package com.example.studybreaktimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    TextView tvMode, tvTimer;
    Button btnStart, btnReset, btnSave;

    CountDownTimer timer;

    long timeLeft;

    String mode;

    int userId;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        tvMode = findViewById(R.id.tvMode);
        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        btnSave = findViewById(R.id.btnSave);

        databaseHelper = new DatabaseHelper(this);

        mode = getIntent().getStringExtra("mode");
        userId = getIntent().getIntExtra("userId",-1);

        tvMode.setText("Mode: " + mode);

        if(mode.equals("study")){
            timeLeft = 25 * 60 * 1000;
        } else {
            timeLeft = 5 * 60 * 1000;
        }

        updateTimer();

        btnStart.setOnClickListener(v -> startTimer());

        btnReset.setOnClickListener(v -> resetTimer());

        btnSave.setOnClickListener(v -> saveSession());

    }

    void startTimer(){

        timer = new CountDownTimer(timeLeft,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                timeLeft = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {

                Toast.makeText(TimerActivity.this,"Time Finished!",Toast.LENGTH_SHORT).show();

            }
        }.start();

    }

    void resetTimer(){

        if(mode.equals("study")){
            timeLeft = 25 * 60 * 1000;
        } else {
            timeLeft = 5 * 60 * 1000;
        }

        updateTimer();

    }

    void updateTimer(){

        int minutes = (int)(timeLeft / 1000) / 60;
        int seconds = (int)(timeLeft / 1000) % 60;

        String time = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        tvTimer.setText(time);

    }

    void saveSession(){

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault()).format(new Date());

        boolean inserted = databaseHelper.insertSession(
                userId,
                "25",
                "5",
                date
        );

        if(inserted){

            Toast.makeText(this,"Session Saved",Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(this,"Error Saving",Toast.LENGTH_SHORT).show();

        }

    }

}