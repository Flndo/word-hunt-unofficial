package com.example.wordhunt;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordhunt.api.GridSetup;
import com.example.wordhunt.api.WordCheck;

import java.util.HashSet;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {
    private static final int gridSize = 5;
    private static final int cellSafeZone = 17;
    private int totalScore = 0;
    private final TextView[][] virtualGrid = new TextView[gridSize][gridSize];
    private final HashSet<TextView> usedLetters = new HashSet<>();
    private final LinkedList<TextView> letterSequence = new LinkedList<TextView>();
    private int previousRow = -1; private int previousColumn = -1;
    private final int tileBackground = R.drawable.tile_background;
    private final int tileBackgroundPressed = R.drawable.tile_background_pressed;
    private final int tileBackgroundPressedOk = R.drawable.tile_background_pressed_ok;
    private final int tileBackgroundPressedUsed = R.drawable.tile_background_pressed_used;
    WordCheck wordCheck;
    TextView scoreTextView, wordsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridSetup gridSetup = new GridSetup();
        gridSetup.fillLetterGrid();
        String[][] letterGrid = gridSetup.getLetterGrid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        wordCheck = WordCheck.wordCheck;

        TextView timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText("02:00");
        scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("0");
        wordsTextView = findViewById(R.id.wordsTextView);
        wordsTextView.setText("0");

        virtualGrid[0][0] = findViewById(R.id.cell00);
        virtualGrid[0][1] = findViewById(R.id.cell01);
        virtualGrid[0][2] = findViewById(R.id.cell02);
        virtualGrid[0][3] = findViewById(R.id.cell03);
        virtualGrid[0][4] = findViewById(R.id.cell04);
        virtualGrid[1][0] = findViewById(R.id.cell10);
        virtualGrid[1][1] = findViewById(R.id.cell11);
        virtualGrid[1][2] = findViewById(R.id.cell12);
        virtualGrid[1][3] = findViewById(R.id.cell13);
        virtualGrid[1][4] = findViewById(R.id.cell14);
        virtualGrid[2][0] = findViewById(R.id.cell20);
        virtualGrid[2][1] = findViewById(R.id.cell21);
        virtualGrid[2][2] = findViewById(R.id.cell22);
        virtualGrid[2][3] = findViewById(R.id.cell23);
        virtualGrid[2][4] = findViewById(R.id.cell24);
        virtualGrid[3][0] = findViewById(R.id.cell30);
        virtualGrid[3][1] = findViewById(R.id.cell31);
        virtualGrid[3][2] = findViewById(R.id.cell32);
        virtualGrid[3][3] = findViewById(R.id.cell33);
        virtualGrid[3][4] = findViewById(R.id.cell34);
        virtualGrid[4][0] = findViewById(R.id.cell40);
        virtualGrid[4][1] = findViewById(R.id.cell41);
        virtualGrid[4][2] = findViewById(R.id.cell42);
        virtualGrid[4][3] = findViewById(R.id.cell43);
        virtualGrid[4][4] = findViewById(R.id.cell44);
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                virtualGrid[row][column].setText(letterGrid[row][column]);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TextView cell = findTextViewByCoordinates(event.getRawX(), event.getRawY());
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int score = wordCheck.wordScore(letterSequence, true);
            totalScore += score;
            scoreTextView.setText(intToString(totalScore));
            while (!letterSequence.isEmpty()) {
                letterSequence.getFirst().setBackground(getDrawable(tileBackground));
                usedLetters.remove(letterSequence.getFirst());
                letterSequence.pop();
            }
            previousColumn = previousRow = -1;
        } else if (cell == null) {
            return super.onTouchEvent(event);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            addLetter(cell);
            int score = wordCheck.wordScore(letterSequence, false);
            if (score == 1) {
                colorUsed(letterSequence);
            } else if (score != 0) {
                colorRight(letterSequence);
            } else {
                colorPressed(letterSequence);
            }
        }
        return super.onTouchEvent(event);
    }

    private TextView findTextViewByCoordinates(float rawX, float rawY) {
        for (int row = 0; row < virtualGrid.length; row++) {
            for (int column = 0; column < virtualGrid[row].length; column++) {
                TextView textView = virtualGrid[row][column];
                int[] location = new int[2];
                textView.getLocationOnScreen(location);

                float textViewX = location[0];
                float textViewY = location[1];

                if (rawX - cellSafeZone >= textViewX && rawX + cellSafeZone <= textViewX + textView.getWidth() &&
                        rawY - cellSafeZone >= textViewY && rawY + cellSafeZone <= textViewY + textView.getHeight()) {
                    if (previousRow == -1 && previousColumn == -1 || (abs(row - previousRow) <= 1 && abs(column - previousColumn) <= 1 && !usedLetters.contains(virtualGrid[row][column]))) {
                        previousRow = row;
                        previousColumn = column;
                        return textView;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    private void addLetter(TextView cell) {
        cell.setBackground(getDrawable(tileBackgroundPressed));
        letterSequence.push(cell);
        usedLetters.add(cell);
    }
    private int abs(int x) {
        if (x < 0) {
            return -x;
        }
        return x;
    }
    private String intToString(int number) {
        return String.valueOf(number);
    }
    private void colorEmpty(LinkedList<TextView> sequence) {
        for (TextView cell: sequence) {
            cell.setBackground(getDrawable(tileBackground));
        }
    }
    private void colorPressed(LinkedList<TextView> sequence) {
        for (TextView cell: sequence) {
            cell.setBackground(getDrawable(tileBackgroundPressed));
        }
    }
    private void colorRight(LinkedList<TextView> sequence) {
        for (TextView cell: sequence) {
            cell.setBackground(getDrawable(tileBackgroundPressedOk));
        }
    }
    private void colorUsed(LinkedList<TextView> sequence) {
        for (TextView cell: sequence) {
            cell.setBackground(getDrawable(tileBackgroundPressedUsed));
        }
    }
}