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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campusdigitalfp.filmoteca.R.string.back_button
import androidx.compose.foundation.lazy.items

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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmListScreen(navController: NavHostController) {
    val films = FilmDataSource.films

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Películas") }
            )
        },
        content = { paddingValues ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(films) { film ->
                    Text(
                        text = film.title ?: "<Sin título>",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
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


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmEditScreen(navController: NavHostController, movieName: String) {

    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var anyo by remember { mutableIntStateOf(1997) }
    var url by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    val context = LocalContext.current
    val generoList = context.resources.getStringArray(R.array.genero_list).toList()
    val formatoList = context.resources.getStringArray(R.array.formato_list).toList()

    var selectedGenero by remember { mutableStateOf(generoList[0]) }
    var generoExpanded by remember { mutableStateOf(false) }

    var selectedFormato by remember { mutableStateOf(formatoList[0]) }
    var formatoExpanded by remember { mutableStateOf(false) }

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
                        painter = painterResource(id = R.drawable.harrypotterpiedrafilosofal),
                        contentDescription = "Cartel de la película",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
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
                    value = anyo.toString(),
                    onValueChange = { if (it.toIntOrNull() != null) anyo = it.toInt() },
                    label = { Text("Año") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Menú desplegable para Género
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = selectedGenero,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(onClick = { generoExpanded = true })
                    )

                    DropdownMenu(
                        expanded = generoExpanded,
                        onDismissRequest = { generoExpanded = false }
                    ) {
                        generoList.forEach { genero ->
                            DropdownMenuItem(text = {Text(genero)}, onClick = {
                                selectedGenero = genero
                                generoExpanded = false
                            })
                        }
                    }
                }


                // Menú desplegable para Formato
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = selectedFormato,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(onClick = { formatoExpanded = true })
                    )

                    DropdownMenu(
                        expanded = formatoExpanded,
                        onDismissRequest = { formatoExpanded = false }
                    ) {
                        formatoList.forEach { formato ->
                            DropdownMenuItem(text = { Text(formato) }, onClick = {
                                selectedFormato = formato
                                formatoExpanded = false
                            })
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
                        onClick = { /* Guardar datos */ },
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

data class Film(
    var id: Int = 0,
    var imageResId: Int = 0, // Propiedades de la clase
    var title: String? = null,
    var director: String? = null,
    var year: Int = 0,
    var genre: Int = 0,
    var format: Int = 0,
    var imdbUrl: String? = null,
    var comments: String? = null
) {
    override fun toString(): String {
        // Al convertir a cadena mostramos su título
        return title ?: "<Sin título>"
    }

    companion object {
        const val FORMAT_DVD = 0 // Formatos
        const val FORMAT_BLURAY = 1
        const val FORMAT_DIGITAL = 2
        const val GENRE_ACTION = 0 // Géneros
        const val GENRE_COMEDY = 1
        const val GENRE_DRAMA = 2
        const val GENRE_SCIFI = 3
        const val GENRE_HORROR = 4
    }
}

object FilmDataSource {
    val films: MutableList<Film> = mutableListOf()

    init {
        // Primera película: Harry Potter y la piedra filosofal
        val f1 = Film()
        f1.id = films.size
        f1.title = "Harry Potter y la piedra filosofal"
        f1.director = "Chris Columbus"
        f1.imageResId = R.drawable.harrypotterpiedrafilosofal
        f1.comments = "Una aventura mágica en Hogwarts."
        f1.format = Film.FORMAT_DVD
        f1.genre = Film.GENRE_ACTION // Cambia según corresponda
        f1.imdbUrl = "http://www.imdb.com/title/tt0241527"
        f1.year = 2001
        films.add(f1)

        // Segunda película: Regreso al futuro
        val f2 = Film()
        f1.id = films.size
        f2.title = "Regreso al futuro"
        f2.director = "Robert Zemeckis"
        f2.imageResId = R.drawable.regresoalfuturo
        f2.comments = ""
        f2.format = Film.FORMAT_DIGITAL
        f2.genre = Film.GENRE_SCIFI
        f2.imdbUrl = "http://www.imdb.com/title/tt0088763"
        f2.year = 1985
        films.add(f2)

        // Tercera película: El rey león
        val f3 = Film()
        f1.id = films.size
        f3.title = "El rey león"
        f3.director = "Roger Allers, Rob Minkoff"
        f3.imageResId = R.drawable.reyleon
        f3.comments = "Una historia de crecimiento y responsabilidad."
        f3.format = Film.FORMAT_BLURAY
        f3.genre = Film.GENRE_ACTION // Cambia según corresponda
        f3.imdbUrl = "http://www.imdb.com/title/tt0110357"
        f3.year = 1994
        films.add(f3)

        // Añade más películas si deseas!
    }
}
