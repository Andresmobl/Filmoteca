package com.campusdigitalfp.filmoteca.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campusdigitalfp.filmoteca.ui.vistas.*
import com.campusdigitalfp.filmoteca.viewmodel.FilmViewModel
import com.campusdigitalfp.filmoteca.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

fun comprobarUsuario(): Boolean {
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser != null
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Navigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val startDestination = if (comprobarUsuario()) "list" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            if (comprobarUsuario()) {
                val filmViewModel: FilmViewModel = viewModel()
                FilmListScreen(navController, filmViewModel, authViewModel)
            } else {
                LoginScreen(navController, authViewModel)
            }
        }

        composable("register") {
            RegisterScreen(navController, authViewModel)
        }

        composable("list") {
            if (comprobarUsuario()) {
                val filmViewModel: FilmViewModel = viewModel()
                FilmListScreen(navController, filmViewModel, authViewModel)
            } else {
                LoginScreen(navController, authViewModel)
            }
        }

        composable("about") {
            if (comprobarUsuario()) {
                AboutScreen(navController)
            } else {
                LoginScreen(navController, authViewModel)
            }
        }

        composable("details/{filmId}") { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId")
            if (comprobarUsuario() && !filmId.isNullOrBlank()) {
                val filmViewModel: FilmViewModel = viewModel()
                val films = filmViewModel.films.collectAsState().value
                val film = films.find { it.id == filmId }
                film?.let {
                    FilmDataScreen(navController, it.id, filmViewModel)
                }
            } else {
                LoginScreen(navController, authViewModel)
            }
        }

        composable("edit/{filmId}") { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId")
            if (comprobarUsuario() && !filmId.isNullOrBlank()) {
                val filmViewModel: FilmViewModel = viewModel()
                val films = filmViewModel.films.collectAsState().value
                val film = films.find { it.id == filmId }
                film?.let {
                    FilmEditScreen(navController, it.id, filmViewModel)
                }
            } else {
                LoginScreen(navController, authViewModel)
            }
        }
    }
}
