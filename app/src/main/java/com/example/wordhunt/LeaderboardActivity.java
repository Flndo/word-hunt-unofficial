package com.example.wordhunt;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordhunt.api.GlobalClasses;
import com.example.wordhunt.api.WordCheck;
import com.example.wordhunt.api.WordHunterDatabaseHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    WordHunterDatabaseHelper wordHunterDatabaseHelper = new WordHunterDatabaseHelper(this);
    private final TextView[][] virtualGrid = new TextView[10][2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        List<Pair<String, Integer>> leaderboardList = wordHunterDatabaseHelper.getTop10Scores();

        for (int row = 0; row < 10 && row < leaderboardList.size(); row++) {
            virtualGrid[row][0] = findViewById(getResources().getIdentifier("leaderboard_cell" + row + 0, "id", getPackageName()));
            virtualGrid[row][1] = findViewById(getResources().getIdentifier("leaderboard_cell" + row + 1, "id", getPackageName()));
            String player = leaderboardList.get(row).first;
            virtualGrid[row][0].setText(player);
            virtualGrid[row][0].setPaddingRelative(20, 0, 20, 0);
            virtualGrid[row][1].setText(intToString(leaderboardList.get(row).second));
        }

        Button exitButton = findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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
            } else {
                return false;
            }
        }
        return true;
    }
}
