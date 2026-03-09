package com.example.studybreaktimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "StudyBreak.db";
    private static final int DB_VERSION = 2;

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    // Session Table
    private static final String TABLE_SESSION = "sessions";
    private static final String COL_SESSION_ID = "session_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_STUDY = "study_time";
    private static final String COL_BREAK = "break_time";
    private static final String COL_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsers = "CREATE TABLE " + TABLE_USERS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USERNAME + " TEXT UNIQUE,"
                + COL_PASSWORD + " TEXT)";

        String createSessions = "CREATE TABLE " + TABLE_SESSION + "("
                + COL_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USER_ID + " INTEGER,"
                + COL_STUDY + " TEXT,"
                + COL_BREAK + " TEXT,"
                + COL_DATE + " TEXT,"
                + "FOREIGN KEY(" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_ID + "))";

        db.execSQL(createUsers);
        db.execSQL(createSessions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }

    // ---------------- PASSWORD HASH ----------------

    private String hashPassword(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();

            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            return password;
        }
    }

    // ---------------- REGISTER USER ----------------

    public boolean registerUser(String username, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, hashPassword(password));

        long result = db.insert(TABLE_USERS, null, values);

        return result != -1;
    }

    // ---------------- LOGIN CHECK ----------------

    public int checkUser(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_USERS + " WHERE username=? AND password=?",
                new String[]{username, hashPassword(password)}
        );

        if (cursor.moveToFirst()) {

            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }

        cursor.close();
        return -1;
    }

    // ---------------- INSERT SESSION ----------------

    public boolean insertSession(int userId, String study, String breakTime, String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_USER_ID, userId);
        values.put(COL_STUDY, study);
        values.put(COL_BREAK, breakTime);
        values.put(COL_DATE, date);

        long result = db.insert(TABLE_SESSION, null, values);

        return result != -1;
    }

    // ---------------- GET USER SESSIONS ----------------

    public Cursor getSessions(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_SESSION + " WHERE user_id=? ORDER BY session_id DESC",
                new String[]{String.valueOf(userId)}
        );
    }

    // ---------------- UPDATE SESSION ----------------

    public boolean updateSession(int sessionId, String study, String breakTime) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_STUDY, study);
        values.put(COL_BREAK, breakTime);

        int result = db.update(
                TABLE_SESSION,
                values,
                COL_SESSION_ID + "=?",
                new String[]{String.valueOf(sessionId)}
        );

        return result > 0;
    }

    // ---------------- DELETE SESSION ----------------

    public boolean deleteSession(int sessionId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_SESSION,
                COL_SESSION_ID + "=?",
                new String[]{String.valueOf(sessionId)}
        );

        return result > 0;
    }
}