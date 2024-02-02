package com.example.wordhunt;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class GameActivity extends AppCompatActivity {
    private static final int gridSize = 5;

    private final TextView[][] virtualGrid = new TextView[gridSize][gridSize];
    private final HashSet<TextView> usedLetters = new HashSet<>();
    private final LinkedList<TextView> letterSequence = new LinkedList<TextView>();
    private int previousRow = -1; private int previousColumn = -1;
    private final int titleBackground = R.drawable.tile_background;
    private final int titleBackgroundPressed = R.drawable.tile_background_pressed;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridSetup gridSetup = new GridSetup();
        gridSetup.fillLetterGrid();
        String[][] letterGrid = gridSetup.getLetterGrid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView timer = findViewById(R.id.timerTextView);
        timer.setText("02:00");


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
                final int c = column;
                final int r = row;
                virtualGrid[row][column].setText(letterGrid[row][column]);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TextView cell = findTextViewByCoordinates(event.getRawX(), event.getRawY());
        if (event.getAction() == MotionEvent.ACTION_UP) {
            while (!letterSequence.isEmpty()) {
                letterSequence.getFirst().setBackground(getDrawable(titleBackground));
                usedLetters.remove(letterSequence.getFirst());
                letterSequence.pop();
            }
            previousColumn = previousRow = -1;
        } else if (cell == null) {
            return super.onTouchEvent(event);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            cell.setBackground(getDrawable(titleBackgroundPressed));
            letterSequence.push(cell);
            usedLetters.add(cell);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            cell.setBackground(getDrawable(titleBackgroundPressed));
            letterSequence.push(cell);
            usedLetters.add(cell);
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

                if (rawX >= textViewX && rawX <= textViewX + textView.getWidth() &&
                        rawY >= textViewY && rawY <= textViewY + textView.getHeight()) {
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

    private int abs(int x) {
        if (x < 0) {
            return -x;
        }
        return x;
    }
}