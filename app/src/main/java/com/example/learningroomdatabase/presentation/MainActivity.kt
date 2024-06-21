package com.example.learningroomdatabase.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.learningroomdatabase.presentation.ui.theme.LearningRoomDataBaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningRoomDataBaseTheme {
                MainScreen()
            }
        }
    }
}
