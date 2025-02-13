package com.campusdigitalfp.filmoteca.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.campusdigitalfp.filmoteca.ui.vistas.AboutScreen
import com.campusdigitalfp.filmoteca.ui.vistas.FilmDataScreen
import com.campusdigitalfp.filmoteca.ui.vistas.FilmEditScreen
import com.campusdigitalfp.filmoteca.ui.vistas.FilmListScreen

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "filmListScreen") {

        // Pantalla principal de lista de películas
        composable("filmListScreen") {
            FilmListScreen(navController = navController)
        }

        // Pantalla de detalles de la película
        composable(
            route = "filmDataScreen/{filmId}",
            arguments = listOf(navArgument("filmId") { type = NavType.IntType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getInt("filmId") ?: -1
            if (filmId >= 0) {
                FilmDataScreen(navController = navController, filmId = filmId)
            } else {
                navController.navigate("filmListScreen") // Redirigir a la lista en caso de error
            }
        }

        // Pantalla de edición de la película
        composable(
            route = "filmEditScreen/{filmId}",
            arguments = listOf(navArgument("filmId") { type = NavType.IntType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getInt("filmId") ?: -1
            if (filmId >= 0) {
                FilmEditScreen(navController = navController, filmId = filmId)
            } else {
                navController.navigate("filmListScreen") // Redirigir a la lista en caso de error
            }
        }

        // Pantalla "Acerca de"
        composable("aboutScreen") {
            AboutScreen(navController)
        }
    }
}