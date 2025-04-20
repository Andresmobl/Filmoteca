package com.campusdigitalfp.filmoteca.ui.vistas

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.campusdigitalfp.filmoteca.model.Film
import com.campusdigitalfp.filmoteca.viewmodel.AuthViewModel
import com.campusdigitalfp.filmoteca.viewmodel.FilmViewModel

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmListScreen(navController: NavHostController, viewModel: FilmViewModel, authViewModel: AuthViewModel = viewModel()) {
    // 🔥 Estado que obtiene la lista de películas en tiempo real desde Firestore
    val films by viewModel.films.collectAsState()

    var isMenuExpanded by remember { mutableStateOf(false) }
    val selectedFilms = remember { mutableStateListOf<String>() } // 🔥 Ahora es `String`, no `Int`

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedFilms.isNotEmpty()) "${selectedFilms.size} seleccionadas"
                        else "Filmoteca"
                    )
                },
                actions = {
                    if (selectedFilms.isNotEmpty()) {
                        IconButton(onClick = {
                            selectedFilms.forEach { id -> viewModel.deleteFilm(id) } // 🔥 Ahora se eliminan desde Firestore
                            selectedFilms.clear()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar seleccionadas")
                        }
                    } else {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Añadir Película") },
                                onClick = {
                                    viewModel.addFilm(
                                        Film(
                                            title = "Nueva Película",
                                            director = "Director por defecto",
                                            image = "defecto", // 🔥 Se usa el nombre del recurso drawable
                                            comments = "Comentarios por defecto",
                                            format = Film.FORMAT_DVD,
                                            genre = Film.GENRE_ACTION,
                                            imdbUrl = "https://www.imdb.com",
                                            year = 2023
                                        )
                                    )
                                    isMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Acerca de") },
                                onClick = {
                                    navController.navigate("aboutScreen")
                                    isMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cerrar Sesion") },
                                onClick = {
                                    isMenuExpanded = false
                                    authViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo("list") { inclusive = true }
                                    }
                                },
                                leadingIcon = {
                                    Icon (
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Cerrar Sesion",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(films) { film ->
                    val isSelected = selectedFilms.contains(film.id) // 🔥 Ahora `id` es `String`

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(if (isSelected) Color(0xFFE8F5E9) else Color.Transparent)
                            .combinedClickable(
                                onClick = {
                                    if (selectedFilms.isNotEmpty()) {
                                        if (isSelected) selectedFilms.remove(film.id)
                                        else selectedFilms.add(film.id)
                                    } else {
                                        navController.navigate("details/${film.id}")
                                    }
                                },
                                onLongClick = {
                                    if (!selectedFilms.contains(film.id)) {
                                        selectedFilms.add(film.id)
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Seleccionado",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(100.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = Film.getImageResource(film.image)), // 🔥 Ahora usamos `getImageResource()`
                                contentDescription = "Cartel de ${film.title}",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = film.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = film.director,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    )
}
