package com.example.studybreaktimer;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    TextView tvHistory;

    DatabaseHelper databaseHelper;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvHistory = findViewById(R.id.tvHistory);

        databaseHelper = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("userId",-1);

        Cursor cursor = databaseHelper.getSessions(userId);

        StringBuilder history = new StringBuilder();

        while(cursor.moveToNext()){

            String study = cursor.getString(2);
            String brk = cursor.getString(3);
            String date = cursor.getString(4);

            history.append("Study: ")
                    .append(study)
                    .append(" min | Break: ")
                    .append(brk)
                    .append(" min\n")
                    .append(date)
                    .append("\n\n");

        }

        tvHistory.setText(history.toString());

        cursor.close();
    }
}