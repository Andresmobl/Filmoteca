package com.campusdigitalfp.filmoteca.ui.vistas

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.campusdigitalfp.filmoteca.model.FilmDataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDataScreen(navController: NavHostController, filmId: Int) {
    val context = LocalContext.current
    val film = FilmDataSource.films.find { it.id == filmId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de la película") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (film != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen de la película
                    Image(
                        painter = painterResource(id = film.imageResId),
                        contentDescription = "Cartel de ${film.title}",
                        modifier = Modifier
                            .size(200.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Director:",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(text = film.director.orEmpty(), style = MaterialTheme.typography.bodyLarge)

                        Text(
                            text = "Año:",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(text = film.year.toString(), style = MaterialTheme.typography.bodyLarge)

                        Text(
                            text = "${film.getFormatName()}, ${film.getGenreName()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Comentarios
                Text(
                    text = "Comentarios:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = film.comments ?: "Sin comentarios",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón "Ver en IMDB"
                Button(
                    onClick = { abrirPaginaWeb(film.imdbUrl, context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Ver en IMDB")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones "Volver" y "Editar"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Volver")
                    }

                    Button(
                        onClick = { navController.navigate("filmEditScreen/${film.id}") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Editar")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Película no encontrada", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

// Función para abrir una página web en el navegador
fun abrirPaginaWeb(url: String?, context: Context) {
    url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        context.startActivity(intent)
    }
}