package com.campusdigitalfp.filmoteca

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campusdigitalfp.filmoteca.R.string.back_button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Configuramos el NavController y el NavHost
            val navController = rememberNavController()
            NavigationGraph(navController = navController)
        }
    }
}

// Función para mostrar un Toast, definida fuera del composable
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Función para abrir una página web
fun abrirPaginaWeb(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

// Función para mandar un correo electrónico
@SuppressLint("QueryPermissionsNeeded")
fun mandarEmail(context: Context, email: String, asunto: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email") // Establece el destinatario del email
        putExtra(Intent.EXTRA_SUBJECT, asunto) // Establece el asunto
    }

    // Verifica si hay una aplicación que pueda manejar el Intent
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent) // Inicia la actividad si hay un manejador
    } else {
        showToast(context, "No se encontró una aplicación de correo electrónico.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Resolvemos los textos fuera de showToast
    val aboutAuthorText = stringResource(id = R.string.about_author)
    val webButtonText = stringResource(id = R.string.web_button)
    val supportButtonText = stringResource(id = R.string.support_button)
    val backButtonText = stringResource(id = back_button)
    val toastMessage = stringResource(id = R.string.toast_functionality_not_implemented)
    val emailSupportSubject = stringResource(id = R.string.incidence_with_film_library) // Asunto del email
    val supportEmail = "andresmorenoblanc1996@gmail.com" // Correo de soporte

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acerca de") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = {

            // Layout de la pantalla
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Texto con nombre de autor
                Text(
                    text = aboutAuthorText,
                    style = TextStyle(fontSize = 24.sp)
                )
                Spacer(modifier = Modifier.height(10.dp)) // Espaciado entre texto e imagen
                // Mostrar imagen
                val image = painterResource(id = R.drawable.perfil)
                Image(
                    painter = image,
                    contentDescription = "Icono Perfil",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(20.dp)) // Espaciado entre imagen y botones

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), //Espaciado entre botones
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Boton Ir al sitio Web
                    Button(
                        onClick = {
                            // Abre la página web cuando se presiona el botón
                            abrirPaginaWeb("https://www.google.es", context) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = webButtonText)
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre botones
                    // Boton "Obtener soporte"
                    Button(
                        onClick = {
                            // Abre la aplicación de correo con el asunto predefinido
                            mandarEmail(context = context, email = supportEmail, asunto = emailSupportSubject) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = supportButtonText)
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre las filas de botones
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Boton "Volver"
                    Button(onClick = {
                        showToast(context = context, message = toastMessage) }) {
                        Text(text = backButtonText)
                    }
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Lista de Películas") },
                actions = {}
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    navController.navigate("filmData/Pelicula A") // Navegar a FilmDataScreen con el nombre de "Pelicula A"
                }) {
                    Text(text = "Ver película A")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    navController.navigate("filmData/Pelicula B") // Navegar a FilmDataScreen con el nombre de "Pelicula B"
                }) {
                    Text(text = "Ver película B")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    navController.navigate("aboutScreen") // Navegar a AboutScreen
                }) {
                    Text(text = "Acerca de")
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDataScreen(navController: NavHostController, movieName: String) {
    val context = LocalContext.current // Extrae el contexto aquí
    // Datos de la película (puedes reemplazar con datos dinámicos)
    val nombre = "Harry Potter y la piedra filosofal"
    val director = "Chris Columbus"
    val releaseYear = "2001"
    val genre = "BluRay, Sci-Fi"
    val imdbLink = "https://www.imdb.com/title/tt0241527/"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de la película: $nombre") },
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
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top
            ) {
                // Row para la imagen y textos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen a la izquierda
                    Image(
                        painter = painterResource(id = R.drawable.harrypotterpiedrafilosofal),
                        contentDescription = "Cartel Pelicula",
                        modifier = Modifier
                            .fillMaxHeight(0.3f) // Usa el 30% de la altura del contenedor
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Fit
                    )

                    // Columna con textos a la derecha de la imagen
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = nombre,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            color = Color.Blue
                        )
                        Text(
                            text = "Director: ",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = director,
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Text(
                            text = "Año: ",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = releaseYear,
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Text(
                            text = genre,
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón debajo
                Button(
                    onClick = {
                        abrirPaginaWeb(imdbLink, context)
                    },
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa todo el ancho
                        .padding(horizontal = 16.dp) // Margen horizontal para que no toque los bordes
                ) {
                    Text(text = "Ver en IMDB")
                }
                Text(
                    text = "Version extendida",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
                // Row con los botones "Volver" y "Editar" ocupando la mitad de espacio cada uno
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre los botones
                ) {
                    // Botón Volver
                    Button(
                        onClick = {
                            navController.navigate("FilmListScreen") // Navega a FilmListScreen
                        },
                        modifier = Modifier.weight(1f) // El botón ocupa la mitad del espacio disponible
                    ) {
                        Text(text = "Volver")
                    }

                    Button(
                        onClick = {
                            navController.navigate("filmEditScreen/$movieName") // Pasar el nombre de la película como argumento
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Editar")
                    }
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmEditScreen(navController: NavHostController, movieName: String) {

    var changesMade by remember { mutableStateOf(false) }  // Para simular cambios
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editando película: $movieName") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Si el usuario presiona el retroceso, devolvemos RESULT_CANCELED
                        navController.previousBackStackEntry?.savedStateHandle?.set("edited", false)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = {

            // UI principal
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Editando película: $movieName", style = TextStyle(fontSize = 24.sp))

                Spacer(modifier = Modifier.height(16.dp))

                // Simulamos que se hacen cambios
                Button(onClick = {
                    changesMade = true  // Simula que se hizo un cambio
                }) {
                    Text(text = "Hacer cambios")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Si se hizo un cambio, devolvemos el resultado de éxito
                    navController.previousBackStackEntry?.savedStateHandle?.set("edited", true)
                    navController.popBackStack()  // Regresamos a la pantalla anterior
                }) {
                    Text(text = "Guardar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Si no se hicieron cambios, devolvemos el resultado de cancelación
                    navController.previousBackStackEntry?.savedStateHandle?.set("edited", false)
                    navController.popBackStack()  // Regresamos a la pantalla anterior
                }) {
                    Text(text = "Cancelar")
                }
            }
        }
    )
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "filmListScreen") {
        composable("filmListScreen") {
            FilmListScreen(navController = navController)
        }
        composable("filmData/{movieName}") { backStackEntry ->
            val movieName = backStackEntry.arguments?.getString("movieName")
            FilmDataScreen(navController = navController, movieName = movieName ?: "Desconocida")
        }
        composable("filmEditScreen/{movieName}") { backStackEntry ->
            val movieName = backStackEntry.arguments?.getString("movieName")
            FilmEditScreen(navController = navController, movieName = movieName ?: "Desconocida")
        }
        composable("aboutScreen") {
            AboutScreen(navController)
        }
    }
}


