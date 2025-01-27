package com.campusdigitalfp.filmoteca

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campusdigitalfp.filmoteca.R.string.back_button
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

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
            FilmotecaAppBar(navController = navController, title = "Acerca de")
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

// ViewModel para gestionar las películas
class FilmViewModel : ViewModel() {
    // Lista de películas
    val films: SnapshotStateList<Film> = mutableStateListOf(*FilmDataSource.films.toTypedArray())

    // Función para añadir una nueva película
    fun addFilm(film: Film) {
        films.add(film)
    }

    // Función para eliminar películas seleccionadas
    fun removeFilms(selectedFilmIds: List<Int>) {
        films.removeAll { it.id in selectedFilmIds }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmListScreen(navController: NavHostController) {
    // Obtener las películas desde FilmDataSource
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
                    // Mostrar papelera si hay películas seleccionadas
                    if (selectedFilms.isNotEmpty()) {
                        IconButton(onClick = {
                            // Elimina las películas seleccionadas
                            films.removeAll { it.id in selectedFilms }
                            FilmDataSource.films.removeAll { it.id in selectedFilms }
                            selectedFilms.clear()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar seleccionadas")
                        }
                    } else {
                        // Menú desplegable normal
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
                                    FilmDataSource.addFilm(
                                        title = "Nueva Película",
                                        director = "Director por defecto",
                                        imageResId = R.drawable.defecto,
                                        comments = "Comentarios por defecto",
                                        format = Film.FORMAT_DVD,
                                        genre = Film.GENRE_ACTION,
                                        imdbUrl = "https://www.imdb.com",
                                        year = 2023
                                    )
                                    films.clear()
                                    films.addAll(FilmDataSource.films) // Refresca la lista local
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
                                        // Alternar selección si estamos en modo selección
                                        if (isSelected) selectedFilms.remove(film.id)
                                        else selectedFilms.add(film.id)
                                    } else {
                                        // Navegar al detalle si no estamos en modo selección
                                        navController.navigate("filmDataScreen/${film.id}")
                                    }
                                },
                                onLongClick = {
                                    // Inicia modo selección al mantener pulsado
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmDataScreen(navController: NavHostController, filmId: Int) {
    val film = FilmDataSource.films.find { it.id == filmId }
        ?: return // Maneja el caso de película no encontrada

    // Obtener contexto para usar en la función abrirPaginaWeb
    val context = LocalContext.current

    Scaffold(
        topBar = {
            FilmotecaAppBar(navController = navController, title = "Datos de la película")
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
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen a la izquierda
                    Image(
                        painter = painterResource(id = film.imageResId),
                        contentDescription = "Cartel de ${film.title}",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Columna con textos a la derecha de la imagen
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Director en negrita
                        Text(
                            text = "Director:",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = film.director ?: "<Desconocido>",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        // Año en negrita
                        Text(
                            text = "Año:",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = film.year.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${film.getFormatName()}, ${film.getGenreName()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Comentarios debajo
                Text(
                    text = film.comments ?: "Sin comentarios",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón "Ver en IMDB"
                Button(
                    onClick = {
                        abrirPaginaWeb(film.imdbUrl?: "", context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp) // Margen horizontal para que no toque los bordes
                ) {
                    Text(text = "Ver en IMDB")
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f) // El botón ocupa la mitad del espacio disponible
                    ) {
                        Text(text = "Volver")
                    }

                    // Botón Editar
                    Button(
                        onClick = {
                            navController.navigate("filmEditScreen/${film.id}")
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
@Composable
fun FilmEditScreen(navController: NavHostController, filmId: Int) {
    val TAG = "FilmEditScreen" // Etiqueta para los logs

    val film = FilmDataSource.films.find { it.id == filmId }
        ?: return // Maneja el caso de película no encontrada

    var titulo by remember { mutableStateOf(film.title) }
    var director by remember { mutableStateOf(film.director) }
    var anyo by remember { mutableIntStateOf(film.year) }
    var url by remember { mutableStateOf(film.imdbUrl) }
    var imagen by remember { mutableIntStateOf(film.imageResId) }
    var comentarios by remember { mutableStateOf(film.comments) }

    var expandedGenero by remember { mutableStateOf(false) }
    var expandedFormato by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val generoList = context.resources.getStringArray(R.array.genero_list).toList()
    val formatoList = context.resources.getStringArray(R.array.formato_list).toList()

    var genero by remember { mutableIntStateOf(film.genre) }
    var formato by remember { mutableIntStateOf(film.format) }

    Scaffold(
        topBar = {
            FilmotecaAppBar(navController = navController, title = "Editar Datos")
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
                        contentDescription = "Cartel de titulo",
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
                    value = titulo ?: "",
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = director ?: "",
                    onValueChange = { director = it },
                    label = { Text("Director") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = anyo.toString(),
                    onValueChange = { newValue ->
                        anyo = newValue.toIntOrNull() ?: anyo
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        Text(text = generoList[genero])
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
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedFormato = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = formatoList[formato])
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
                    value = url ?: "",
                    onValueChange = { url = it },
                    label = { Text("Enlace a IMDB") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = comentarios?: "",
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
                            // Encuentra el índice de la película seleccionada
                            val filmIndex = FilmDataSource.films.indexOfFirst { it.id == filmId }
                            if (filmIndex != -1) {
                                // Actualiza los datos en la lista
                                FilmDataSource.films[filmIndex] = Film(
                                    id = filmId,
                                    imageResId = imagen,
                                    title = titulo,
                                    director = director,
                                    year = anyo,
                                    genre = genero,
                                    format = formato,
                                    imdbUrl = url,
                                    comments = comentarios
                                )

                                // Muestra un Toast indicando que los datos han sido guardados
                                Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()

                                // Log de guardado de datos
                                Log.i(TAG, "Cambios guardados para la película con ID: $filmId")
                            }

                            // Vuelve a la pantalla anterior
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            // Log de cancelación de cambios
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
            val filmId = backStackEntry.arguments?.getInt("filmId")
            if (filmId != null && filmId >= 0) {
                FilmDataScreen(navController = navController, filmId = filmId)
            } else {
                navController.popBackStack() // Si el ID no es válido, regresar
            }
        }

        // Pantalla de edición de la película
        composable(
            route = "filmEditScreen/{filmId}",
            arguments = listOf(navArgument("filmId") { type = NavType.IntType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getInt("filmId")
            if (filmId != null && filmId >= 0) {
                FilmEditScreen(navController = navController, filmId = filmId)
            } else {
                navController.popBackStack() // Si el ID no es válido, regresar
            }
        }

        // Pantalla "Acerca de"
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

    // Función para convertir el formato a texto
    fun getFormatName(): String {
        return when (format) {
            FORMAT_DVD -> "DVD"
            FORMAT_BLURAY -> "Blu-ray"
            FORMAT_DIGITAL -> "Digital"
            else -> "Desconocido"
        }
    }

    // Función para convertir el género a texto
    fun getGenreName(): String {
        return when (genre) {
            GENRE_ACTION -> "Acción"
            GENRE_COMEDY -> "Comedia"
            GENRE_DRAMA -> "Drama"
            GENRE_SCIFI -> "Ciencia Ficción"
            GENRE_HORROR -> "Terror"
            else -> "Desconocido"
        }
    }
}

object FilmDataSource {
    val films: MutableList<Film> = mutableListOf()

    init {
        // Primera película: Harry Potter y la piedra filosofal
        addFilm(
            title = "Harry Potter y la piedra filosofal",
            director = "Chris Columbus",
            imageResId = R.drawable.harrypotterpiedrafilosofal,
            comments = "Una aventura mágica en Hogwarts.",
            format = Film.FORMAT_DVD,
            genre = Film.GENRE_ACTION,
            imdbUrl = "http://www.imdb.com/title/tt0241527",
            year = 2001
        )

        // Segunda película: Regreso al futuro
        addFilm(
            title = "Regreso al futuro",
            director = "Robert Zemeckis",
            imageResId = R.drawable.regresoalfuturo,
            comments = "Una aventura épica en el tiempo.",
            format = Film.FORMAT_DIGITAL,
            genre = Film.GENRE_SCIFI,
            imdbUrl = "http://www.imdb.com/title/tt0088763",
            year = 1985
        )

        // Tercera película: El rey león
        addFilm(
            title = "El Rey León",
            director = "Roger Allers, Rob Minkoff",
            imageResId = R.drawable.reyleon,
            comments = "Una historia de crecimiento y responsabilidad.",
            format = Film.FORMAT_BLURAY,
            genre = Film.GENRE_DRAMA,
            imdbUrl = "http://www.imdb.com/title/tt0110357",
            year = 1994
        )
    }

    // Función para añadir una película con datos
    fun addFilm(
        title: String,
        director: String,
        imageResId: Int,
        comments: String,
        format: Int,
        genre: Int,
        imdbUrl: String,
        year: Int
    ) {
        val newFilm = Film(
            id = (films.maxOfOrNull { it.id } ?: 0) + 1, // Generar ID único
            title = title,
            director = director,
            imageResId = imageResId,
            comments = comments,
            format = format,
            genre = genre,
            imdbUrl = imdbUrl,
            year = year
        )
        films.add(newFilm)
    }
}

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
                                popUpTo("filmListScreen") { inclusive = true } // Evita volver atrás
                            }
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home, // Ícono de casa
                        contentDescription = "Volver al listado",
                        modifier = Modifier.size(24.dp), // Tamaño del ícono
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(title) // Título de la pantalla
            }
        }
    )
}
