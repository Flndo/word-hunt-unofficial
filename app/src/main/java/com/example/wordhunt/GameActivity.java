package com.example.wordhunt;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordhunt.api.GridSetup;
import com.example.wordhunt.api.WordCheck;

import java.util.HashSet;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {
    boolean layoutLarge = false;
    private static final int gridSize = 5;
    private static final int cellSafeZone = 17;
    private int totalScore = 0;
    private final TextView[][] virtualGrid = new TextView[gridSize][gridSize];
    private final HashSet<TextView> usedLetters = new HashSet<>();
    private final LinkedList<TextView> letterSequence = new LinkedList<>();
    private int previousRow = -1;
    private int previousColumn = -1;
    private final int tileBackground = R.drawable.tile_background;
    private final int tileBackgroundPressed = R.drawable.tile_background_pressed;
    private final int tileBackgroundPressedOk = R.drawable.tile_background_pressed_ok;
    private final int tileBackgroundPressedUsed = R.drawable.tile_background_pressed_used;
    private final int tileBackgroundLarge = R.drawable.tile_background_large;
    private final int tileBackgroundPressedLarge = R.drawable.tile_background_pressed_large;
    private final int tileBackgroundPressedOkLarge = R.drawable.tile_background_pressed_ok_large;
    private final int tileBackgroundPressedUsedLarge = R.drawable.tile_background_pressed_used_large;
    WordCheck wordCheck;
    TextView scoreTextView, wordsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Configuration configuration = getResources().getConfiguration();
        int screenSize = configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            layoutLarge = true;
        }

        GridSetup gridSetup = new GridSetup();
        gridSetup.fillLetterGrid();
        String[][] letterGrid = gridSetup.getLetterGrid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        wordCheck = WordCheck.wordCheck;

        scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("0");
        wordsTextView = findViewById(R.id.wordsTextView);
        wordsTextView.setText("0");

        usedLetters.clear();
        letterSequence.clear();
        WordCheck.wordCheck.clearGame();

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                virtualGrid[row][column] = findViewById(getResources().getIdentifier("cell" + row + column, "id", getPackageName()));
                virtualGrid[row][column].setText(letterGrid[row][column]);
            }
        }

        TextView timerTextView = findViewById(R.id.timerTextView);
        startTimer(timerTextView, 120000); //120000 milliseconds = 2 minutes
    }

    private void startTimer(TextView timerTextView, long timeLengthMilliseconds) {
        new CountDownTimer(timeLengthMilliseconds, 1000) { // 1000ms interval (1 second)
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                timerTextView.setText(String.format(getString(R.string.timeTemplate), secondsRemaining / 60, secondsRemaining % 60));
            }

            @Override
            public void onFinish() {
                timerTextView.setText(R.string.zeroTime);

                String congratulationMessage = "Congratulations! Your score is: " + totalScore;

                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("Time's Up!");
                builder.setMessage(congratulationMessage);
                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                });
                builder.setIcon(android.R.drawable.star_big_on);
                builder.show();
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TextView cell = findTextViewByCoordinates(event.getRawX(), event.getRawY());
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int score = wordCheck.wordScore(letterSequence, true);
            totalScore += score;
            scoreTextView.setText(intToString(totalScore));
            colorEmpty(letterSequence);
            while (!letterSequence.isEmpty()) {
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
        if (layoutLarge) {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackgroundLarge));
            }
        }
        else {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackground));
            }
        }
    }

    private void colorPressed(LinkedList<TextView> sequence) {
        if (layoutLarge) {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackgroundPressedLarge));
            }
        }
        else {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackgroundPressed));
            }
        }
    }

    private void colorRight(LinkedList<TextView> sequence) {
        if (layoutLarge) {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackgroundPressedOkLarge));
            }
        }
        else {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackgroundPressedOk));
            }
        }
    }

    private void colorUsed(LinkedList<TextView> sequence) {
        if (layoutLarge) {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackgroundPressedUsedLarge));
            }
        }
        else {
            for (TextView cell : sequence) {
                cell.setBackground(getDrawable(tileBackgroundPressedUsed));
            }
        }
    }
}