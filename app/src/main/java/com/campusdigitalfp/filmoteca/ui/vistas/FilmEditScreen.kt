package com.campusdigitalfp.filmoteca.ui.vistas

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.campusdigitalfp.filmoteca.model.Film
import com.campusdigitalfp.filmoteca.viewmodel.FilmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmEditScreen(navController: NavHostController, filmId: String, viewModel: FilmViewModel) {

    val context = LocalContext.current

    // 🔥 Obtener la película desde Firestore en tiempo real
    val films = viewModel.films.collectAsState().value
    val film = films.find { it.id == filmId } ?: return

    // 🔥 Estados para los datos editables
    var titulo by remember { mutableStateOf(film.title) }
    var director by remember { mutableStateOf(film.director) }
    var anyo by remember { mutableStateOf(film.year.toString()) }
    var url by remember { mutableStateOf(film.imdbUrl) }
    var comentarios by remember { mutableStateOf(film.comments) }
    val imagen by remember { mutableStateOf(film.image) }

    var expandedGenero by remember { mutableStateOf(false) }
    var expandedFormato by remember { mutableStateOf(false) }

    // 🔥 Listas de géneros y formatos
    val generoList = listOf(Film.GENRE_ACTION, Film.GENRE_COMEDY, Film.GENRE_DRAMA, Film.GENRE_SCIFI, Film.GENRE_HORROR)
    val formatoList = listOf(Film.FORMAT_DVD, Film.FORMAT_BLURAY, Film.FORMAT_DIGITAL)

    var genero by remember { mutableStateOf(film.genre) }
    var formato by remember { mutableStateOf(film.format) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Película") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 🔥 Obtener la imagen desde Firestore
                    Image(
                        painter = painterResource(id = Film.getImageResource(imagen)),
                        contentDescription = "Cartel de la película",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                // 🔥 Campos editables
                TextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())

                TextField(value = director, onValueChange = { director = it }, label = { Text("Director") }, modifier = Modifier.fillMaxWidth())

                TextField(value = anyo, onValueChange = { anyo = it.filter { it.isDigit() } }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), label = { Text("Año") }, modifier = Modifier.fillMaxWidth())

                // 🔥 Menú desplegable para Género
                Column {
                    Text("Género")
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .clickable { expandedGenero = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = genero)
                    }
                    DropdownMenu(expanded = expandedGenero, onDismissRequest = { expandedGenero = false }) {
                        generoList.forEach { item ->
                            DropdownMenuItem(text = { Text(item) }, onClick = { genero = item; expandedGenero = false })
                        }
                    }
                }

                // 🔥 Menú desplegable para Formato
                Column {
                    Text("Formato")
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .clickable { expandedFormato = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = formato)
                    }
                    DropdownMenu(expanded = expandedFormato, onDismissRequest = { expandedFormato = false }) {
                        formatoList.forEach { item ->
                            DropdownMenuItem(text = { Text(item) }, onClick = { formato = item; expandedFormato = false })
                        }
                    }
                }

                TextField(value = url, onValueChange = { url = it }, label = { Text("Enlace a IMDB") }, modifier = Modifier.fillMaxWidth())

                TextField(value = comentarios, onValueChange = { comentarios = it }, label = { Text("Comentarios") }, modifier = Modifier.fillMaxWidth())

                // 🔥 Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            val updatedFilm = Film(
                                id = filmId,
                                title = titulo,
                                director = director,
                                year = anyo.toIntOrNull() ?: film.year,
                                genre = genero,
                                format = formato,
                                imdbUrl = url,
                                comments = comentarios,
                                image = imagen
                            )

                            viewModel.updateFilm(updatedFilm) // 🔥 Actualizar en Firestore
                            Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    )
}