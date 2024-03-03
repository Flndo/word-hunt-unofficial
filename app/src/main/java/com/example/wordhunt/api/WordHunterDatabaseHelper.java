package com.example.wordhunt.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WordHunterDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wordHunter.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COLUMN_SCORE = "score";

    // SQL statement for creating the scores table
    private static final String TABLE_CREATE = 
            "CREATE TABLE " + TABLE_SCORES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_NAME + " TEXT, " +
            COLUMN_SCORE + " INTEGER" +
            ")";

    public WordHunterDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public void addScore(String userName, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_SCORE, score);

        db.insert(TABLE_SCORES, null, values);
        db.close();
    }

    public List<String> getTop10Scores() {
        List<String> topScores = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SCORES + " ORDER BY " + COLUMN_SCORE + " DESC LIMIT 10";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int scoreIndex = cursor.getColumnIndex(COLUMN_SCORE);

            // Ensure both indices are found
            if (userNameIndex != -1 && scoreIndex != -1) {
                do {
                    String userScore = cursor.getString(userNameIndex) + ": " +
                            cursor.getInt(scoreIndex);
                    topScores.add(userScore);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        return topScores;
    }
}
