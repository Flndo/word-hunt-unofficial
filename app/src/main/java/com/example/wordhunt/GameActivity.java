package com.example.wordhunt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity {

    private final TextView[][] virtualGrid = new TextView[5][5];

    @SuppressLint("MissingInflatedId")
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
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                virtualGrid[row][column].setText(letterGrid[row][column]);
            }
        }
    }
}