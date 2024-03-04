package com.example.wordhunt.api;

public class GlobalClasses {
    private static WordCheck wordCheck;
    public static WordCheck wordCheck() {
        return wordCheck;
    }
    public static void setWordCheck(WordCheck instance) {
        wordCheck = instance;
    }
}
