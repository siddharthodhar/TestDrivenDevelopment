package com.example.tdd.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.tdd.model.RetrofitBuilder
import com.example.tdd.model.RoomDatabaseBuilder
import com.example.tdd.model.TDDRepositoryImpl
import com.example.tdd.model.data.local.datasource.TDDLocalDataSource
import com.example.tdd.model.data.remote.datasource.TDDAPIDataSource
import com.example.tdd.ui.theme.TDDTheme

class TDDActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repo = TDDRepositoryImpl(
            remote = TDDAPIDataSource(RetrofitBuilder.service),
            local = TDDLocalDataSource(RoomDatabaseBuilder.getTDDDao())
        )

        setContent {
            TDDTheme {
                val navController = rememberNavController()
                TDDNavHost(navController = navController, repo = repo)
            }
        }
    }
}