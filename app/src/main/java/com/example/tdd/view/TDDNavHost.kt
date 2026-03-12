package com.example.tdd.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tdd.view.composables.DetailScreen
import com.example.tdd.view.composables.HomeScreen
import com.example.tdd.viewmodels.DetailScreenViewModel
import com.example.tdd.viewmodels.HomeScreenViewModel

@Composable
fun TDDNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = hiltViewModel<HomeScreenViewModel>(),
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            DetailScreen(
                viewModel = hiltViewModel<DetailScreenViewModel>(backStackEntry),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

