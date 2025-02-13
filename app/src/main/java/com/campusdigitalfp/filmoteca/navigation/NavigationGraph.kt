package com.campusdigitalfp.filmoteca.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.campusdigitalfp.filmoteca.ui.vistas.*
import com.campusdigitalfp.filmoteca.viewmodel.FilmViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun NavigationGraph(navController: NavHostController, filmViewModel: FilmViewModel) {
    NavHost(navController = navController, startDestination = "filmListScreen") {

        composable("filmListScreen") {
            FilmListScreen(navController = navController, viewModel = filmViewModel)
        }

        composable(
            route = "filmDataScreen/{filmId}",
            arguments = listOf(navArgument("filmId") { type = NavType.StringType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId")
            if (filmId != null) {
                FilmDataScreen(navController = navController, filmId = filmId, viewModel = filmViewModel)
            }
        }

        composable(
            route = "filmEditScreen/{filmId}",
            arguments = listOf(navArgument("filmId") { type = NavType.StringType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId")
            if (filmId != null) {
                FilmEditScreen(navController = navController, filmId = filmId, viewModel = filmViewModel)
            }
        }

        composable("aboutScreen") {
            AboutScreen(navController)
        }
    }
}