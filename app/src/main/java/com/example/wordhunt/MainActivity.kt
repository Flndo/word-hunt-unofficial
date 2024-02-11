package com.example.wordhunt

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wordhunt.api.WordCheck
import com.example.wordhunt.ui.theme.WordHuntTheme

class MainActivity : ComponentActivity() {
    var layoutLarge = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WordCheck.wordCheck.importDictionary(this);
        setContent {
            WordHuntTheme {
                setContentView(R.layout.activity_main);
            }
        }
    }

    fun startGame(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}