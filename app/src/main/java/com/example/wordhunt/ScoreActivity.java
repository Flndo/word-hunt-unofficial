package com.example.wordhunt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.SortedSet;

public class ScoreActivity extends AppCompatActivity {
    public int score = 0;
    public SortedSet<String> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }
}
