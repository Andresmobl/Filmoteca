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
import androidx.navigation.NavHostController
import com.campusdigitalfp.filmoteca.R
import com.campusdigitalfp.filmoteca.model.Film
import com.campusdigitalfp.filmoteca.model.FilmDataSource

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmListScreen(navController: NavHostController) {
    // Estado que mantiene la lista de películas y se actualiza en tiempo real
    val films = remember { mutableStateListOf(*FilmDataSource.films.toTypedArray()) }

    var isMenuExpanded by remember { mutableStateOf(false) }
    val selectedFilms = remember { mutableStateListOf<Int>() } // IDs seleccionados

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
                            films.removeAll { it.id in selectedFilms }
                            FilmDataSource.films.removeAll { it.id in selectedFilms }
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
                                    val newFilm = Film(
                                        id = (films.maxOfOrNull { it.id } ?: 0) + 1, // Generar un ID único
                                        title = "Nueva Película",
                                        director = "Director por defecto",
                                        imageResId = R.drawable.defecto,
                                        comments = "Comentarios por defecto",
                                        format = Film.FORMAT_DVD,
                                        genre = Film.GENRE_ACTION,
                                        imdbUrl = "https://www.imdb.com",
                                        year = 2023
                                    )
                                    FilmDataSource.films.add(newFilm) // Agregar la película a la fuente de datos
                                    films.add(newFilm) // Agregar la película a la lista en pantalla
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
                    val isSelected = film.id in selectedFilms
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
                                        navController.navigate("filmDataScreen/${film.id}")
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
                                painter = painterResource(id = film.imageResId),
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
                                text = film.title ?: "<Sin título>",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = film.director ?: "<Sin director>",
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
