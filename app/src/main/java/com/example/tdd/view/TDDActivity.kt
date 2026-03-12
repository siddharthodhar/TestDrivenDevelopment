package com.example.tdd.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.tdd.ui.theme.TDDTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TDDActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TDDTheme {
                val navController = rememberNavController()
                TDDNavHost(navController = navController)
            }
        }
    }
}