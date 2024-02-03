package com.example.wordhunt.api;

import java.util.Random;

public class GridSetup {
    int currentLetter;
    public GridSetup() {
        currentLetter = 0;
        char addedLetter = 'a';
        for (int ratio = 0; ratio < letterAmount; ratio++) {
            for (int count = 0; count < letterRatio[ratio]; count++) {
                letterList[currentLetter++] = addedLetter;
            }
            addedLetter++;
        }
    }
    private static final int gridCoulmns = 5;
    private static final int gridRows = 5;
    private static final int letterAmount = 26;
    private char[][] letterGrid = new char[gridRows][gridCoulmns];
    private int[] letterRatio = new int[] {14, 3, 9, 7, 18, 3, 2, 6, 6, 1, 1, 5, 7, 8, 9, 8, 1, 5, 10, 9, 1, 1, 1, 1, 1, 1};
    private char[] letterList = new char[200];
    private Random randomChooser = new Random();
    private char getRandomLetter() {
        return letterList[randomChooser.nextInt(currentLetter)];
    }
    public void fillLetterGrid() {
        for (int row = 0; row < gridRows; row++) {
            for (int column = 0; column < gridCoulmns; column++) {
                letterGrid[row][column] = getRandomLetter();
            }
        }
    }

    public String[][] getLetterGrid() {
        String[][] returnString = new String[gridCoulmns][gridRows];
        for (int row = 0; row < gridRows; row++) {
            for (int column = 0; column < gridCoulmns; column++) {
                returnString[row][column] = String.valueOf(letterGrid[row][column]).toUpperCase();
            }
        }
        return returnString;
    }
}
