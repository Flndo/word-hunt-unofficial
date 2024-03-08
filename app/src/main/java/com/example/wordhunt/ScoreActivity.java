package com.example.wordhunt;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordhunt.api.GlobalClasses;
import com.example.wordhunt.api.WordCheck;
import com.example.wordhunt.api.WordHunterDatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

public class ScoreActivity extends AppCompatActivity {
    public int score = 0;
    WordCheck wordCheck;
    WordHunterDatabaseHelper wordHunterDatabaseHelper = new WordHunterDatabaseHelper(this);
    private final TextView[][] virtualGrid = new TextView[11][2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        wordCheck = GlobalClasses.wordCheck();

        List<String> wordList = new ArrayList<>(wordCheck.wordUsed);
        wordList.sort(new StringComparator());
        final int[] lengthScore = wordCheck.lengthScore;
        for (String word: wordList) {
            score += lengthScore[word.length()];
        }

        TextView scoreValue = findViewById(R.id.score_count_value);
        scoreValue.setText(intToString(score));
        TextView wordValue = findViewById(R.id.score_word_count);
        wordValue.setText(intToString(wordList.size()));

        for (int row = 0; row <= 10 && row < wordList.size(); row++) {
            virtualGrid[row][0] = findViewById(getResources().getIdentifier("score_cell" + row + 0, "id", getPackageName()));
            virtualGrid[row][1] = findViewById(getResources().getIdentifier("score_cell" + row + 1, "id", getPackageName()));
            String word = wordList.get(row);
            virtualGrid[row][0].setText(word);
            virtualGrid[row][0].setPaddingRelative(20, 0, 20, 0);
            virtualGrid[row][1].setText(intToString(lengthScore[word.length()]));
        }

        Button exitButton = findViewById(R.id.button_exit);
        Button saveButton = findViewById(R.id.button_save);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);
                builder.setTitle("Enter your name");
                builder.setMessage("Please use only English letters or numerals");

                final EditText input = new EditText(ScoreActivity.this);
                builder.setView(input);

                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String playerName = input.getText().toString();
                    if (validWord(playerName)) {
                        wordHunterDatabaseHelper.addScore(playerName, score); //crashes
                    }
                    Intent intent = new Intent(ScoreActivity.this, LeaderboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                });
                builder.setIcon(android.R.drawable.star_big_on);
                builder.show();
            }
        });
    }
    static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            int lengthComparison = Integer.compare(s1.length(), s2.length());
            if (lengthComparison != 0) {
                return ~lengthComparison;
            }
            return s1.compareTo(s2);
        }
    }
    private String intToString(int number) {
        return String.valueOf(number);
    }
    private boolean validWord(String word) {
        for (int letter = 0; letter < word.length(); letter++) {
            char symbol = word.charAt(letter);
            if ((symbol >= '0' && symbol <= '9') || (symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z')) {
                continue;
            } else if (symbol == '_' || symbol == '-' || symbol == ' ') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
