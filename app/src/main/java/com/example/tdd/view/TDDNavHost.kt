package com.example.tdd.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tdd.view.composables.DetailScreen
import com.example.tdd.view.composables.HomeScreen
import com.example.tdd.viewmodels.DetailScreenViewModelFactory
import com.example.tdd.viewmodels.HomeScreenViewModelFactory
import com.example.tdd.viewmodels.domain.TDDRepository

@Composable
fun TDDNavHost(
    navController: NavHostController,
    repo: TDDRepository
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel(factory = HomeScreenViewModelFactory(repo)),
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            DetailScreen(
                viewModel = viewModel(factory = DetailScreenViewModelFactory(repo, id)),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

