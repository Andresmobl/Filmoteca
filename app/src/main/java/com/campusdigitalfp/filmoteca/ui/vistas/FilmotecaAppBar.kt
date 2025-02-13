package com.campusdigitalfp.filmoteca.ui.vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmotecaAppBar(navController: NavHostController, title: String) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            // Navega al listado de películas
                            navController.navigate("filmListScreen") {
                                popUpTo("filmListScreen") { saveState = true }
                                launchSingleTop = true
                            }
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Ir a la lista de películas",
                        modifier = Modifier.size(24.dp),
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(title) // Título de la pantalla
            }
        }
    )
}