package com.example.wordhunt.api;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;

public class WordCheck {

    public static WordCheck wordCheck = new WordCheck();

    private HashSet<String> wordList = new HashSet<>();
    private HashSet<String> wordUsed = new HashSet<>();
    public final String dictionaryPath = "wordDictionary.txt";
    private final int[] lengthScore = new int[26];
    public WordCheck() {
        lengthScore[3] = 100;
        lengthScore[4] = 400;
        lengthScore[5] = 800;
        int score = 1400;
        for (int length = 6; length <= 25; length++) {
            lengthScore[length] = score;
            score += 400;
        }
    }

    public int wordScore(LinkedList<TextView> wordLinked, boolean finalized) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TextView letterView: wordLinked) {
            stringBuilder.append(letterView.getText());
        }
        String word = stringBuilder.reverse().toString();
        if (!wordList.contains(word)) {
            return 0;
        }  else if (wordUsed.contains(word)) {
            return finalized ? 0 : 1;
        } else {
            if (finalized) {
                wordUsed.add(word);
            }
            return lengthScore[word.length()];
        }
    }
    public void importDictionary(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(dictionaryPath)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    wordList.add(word.trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
