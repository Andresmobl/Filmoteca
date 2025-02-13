package com.campusdigitalfp.filmoteca.ui.vistas

import android.annotation.SuppressLint
import android.util.Log
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
import com.campusdigitalfp.filmoteca.model.FilmDataSource

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmEditScreen(navController: NavHostController, filmId: Int) {
    val TAG = "FilmEditScreen"

    val film = FilmDataSource.films.find { it.id == filmId }
        ?: return // Maneja el caso en que la película no exista

    var titulo by remember { mutableStateOf(film.title.orEmpty()) }
    var director by remember { mutableStateOf(film.director.orEmpty()) }
    var anyo by remember { mutableStateOf(film.year.toString()) }
    var url by remember { mutableStateOf(film.imdbUrl.orEmpty()) }
    var comentarios by remember { mutableStateOf(film.comments.orEmpty()) }
    var imagen by remember { mutableStateOf(film.imageResId) }

    var expandedGenero by remember { mutableStateOf(false) }
    var expandedFormato by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val generoList = listOf("Acción", "Comedia", "Drama", "Ciencia Ficción", "Terror")
    val formatoList = listOf("DVD", "Blu-ray", "Digital")

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
                    Image(
                        painter = painterResource(id = imagen),
                        contentDescription = "Cartel de la película",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { /* Capturar fotografía */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Capturar fotografía", color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { /* Seleccionar imagen */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Seleccionar imagen", color = Color.White)
                        }
                    }
                }

                TextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = director,
                    onValueChange = { director = it },
                    label = { Text("Director") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = anyo,
                    onValueChange = { newValue -> anyo = newValue.filter { it.isDigit() } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Menú desplegable para Género
                Column {
                    Text("Género")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedGenero = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = generoList.getOrNull(genero) ?: "Desconocido")
                    }
                    DropdownMenu(
                        expanded = expandedGenero,
                        onDismissRequest = { expandedGenero = false }
                    ) {
                        generoList.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    genero = index
                                    expandedGenero = false
                                }
                            )
                        }
                    }
                }

                // Menú desplegable para Formato
                Column {
                    Text("Formato")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedFormato = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = formatoList.getOrNull(formato) ?: "Desconocido")
                    }
                    DropdownMenu(
                        expanded = expandedFormato,
                        onDismissRequest = { expandedFormato = false }
                    ) {
                        formatoList.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    formato = index
                                    expandedFormato = false
                                }
                            )
                        }
                    }
                }

                TextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Enlace a IMDB") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = comentarios,
                    onValueChange = { comentarios = it },
                    label = { Text("Comentarios") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            val filmIndex = FilmDataSource.films.indexOfFirst { it.id == filmId }
                            if (filmIndex != -1) {
                                FilmDataSource.films[filmIndex] = Film(
                                    id = filmId,
                                    imageResId = imagen,
                                    title = titulo,
                                    director = director,
                                    year = anyo.toIntOrNull() ?: film.year,
                                    genre = genero,
                                    format = formato,
                                    imdbUrl = url,
                                    comments = comentarios
                                )

                                Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                                Log.i(TAG, "Cambios guardados para la película con ID: $filmId")
                            }

                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            Log.i(TAG, "Cambios descartados para la película con ID: $filmId")
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    )
}