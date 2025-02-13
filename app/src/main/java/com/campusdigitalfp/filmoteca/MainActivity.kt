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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance() // Inicializa Firestore

        setContent {
            val navController = rememberNavController() // Controlador de navegación
            val filmViewModel: FilmViewModel = viewModel() // Instancia de ViewModel

            NavigationGraph(navController = navController, filmViewModel = filmViewModel)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmListScreen(navController: NavHostController, viewModel: FilmViewModel = viewModel()) {
    val films by viewModel.films.collectAsState()
    var isMenuExpanded by remember { mutableStateOf(false) }
    val selectedFilms = remember { mutableStateListOf<String>() }

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
                            selectedFilms.forEach { id -> viewModel.deleteFilm(id) }
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
                                            comments = "Comentarios por defecto",
                                            format = Film.FORMAT_DVD,
                                            genre = Film.GENRE_ACTION,
                                            imdbUrl = "https://www.imdb.com",
                                            year = 2023,
                                            image = "defecto" // Asigna un nombre de imagen por defecto
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
                    val isSelected = film.id?.let { selectedFilms.contains(it) } ?: false
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(if (isSelected) Color(0xFFE8F5E9) else Color.Transparent)
                            .combinedClickable(
                                onClick = {
                                    if (selectedFilms.isNotEmpty()) {
                                        film.id?.let {
                                            if (isSelected) selectedFilms.remove(it)
                                            else selectedFilms.add(it)
                                        }
                                    } else {
                                        film.id?.let { navController.navigate("filmDataScreen/$it") }
                                    }
                                },
                                onLongClick = {
                                    film.id?.let {
                                        if (!selectedFilms.contains(it)) {
                                            selectedFilms.add(it)
                                        }
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
                                painter = painterResource(id = Film.getImageResource(film.image)),
                                contentDescription = "Cartel de ${film.title}",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = film.title, style = MaterialTheme.typography.bodyLarge)
                            Text(text = film.director, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FilmDataScreen(navController: NavHostController, filmId: String, viewModel: FilmViewModel = viewModel()) {
    val films by viewModel.films.collectAsState() // Obtenemos la lista de películas en tiempo real
    val film = films.find { it.id == filmId } ?: return // Buscar película por ID

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Obtener el recurso de imagen según el campo `image` en Firestore
                    val imageResId = Film.getImageResource(film.image)

                    // Imagen de la película
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Cartel de ${film.title}",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(8.dp))
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
                        Text(
                            text = film.director,
                            style = MaterialTheme.typography.bodyLarge
                        )
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

                // Comentarios
                Text(
                    text = film.comments,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón "Ver en IMDB"
                Button(
                    onClick = {
                        abrirPaginaWeb(film.imdbUrl, context)
                    },
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
                        onClick = {
                            film.id?.let { navController.navigate("filmEditScreen/$it") }
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
fun FilmEditScreen(navController: NavHostController, filmId: String, viewModel: FilmViewModel = viewModel()) {
    val TAG = "FilmEditScreen"

    val films by viewModel.films.collectAsState() // Obtener películas en tiempo real
    val film = films.find { it.id == filmId } ?: return // Buscar película por ID en Firestore

    var titulo by remember { mutableStateOf(film.title) }
    var director by remember { mutableStateOf(film.director) }
    var anyo by remember { mutableStateOf(film.year.toString()) }
    var url by remember { mutableStateOf(film.imdbUrl) }
    var comentarios by remember { mutableStateOf(film.comments) }
    var imagen by remember { mutableStateOf(film.image) } // Ahora se usa el campo `image`

    var expandedGenero by remember { mutableStateOf(false) }
    var expandedFormato by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val generoList = listOf("Acción", "Comedia", "Drama", "Ciencia Ficción", "Terror")
    val formatoList = listOf("DVD", "Blu-ray", "Digital")

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
                    val imageResId = Film.getImageResource(imagen) // Cambiado a `imagen`

                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Cartel de $titulo",
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
                    onValueChange = { newValue ->
                        anyo = newValue.filter { it.isDigit() } // Solo números
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
                    Box(
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
                            // Guardar cambios en Firestore
                            viewModel.updateFilm(
                                Film(
                                    id = filmId,
                                    title = titulo,
                                    director = director,
                                    year = anyo.toIntOrNull() ?: film.year,
                                    genre = genero,
                                    format = formato,
                                    imdbUrl = url,
                                    comments = comentarios,
                                    image = imagen // Ahora se guarda `image` en Firestore
                                )
                            )

                            Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                            Log.i(TAG, "Cambios guardados para la película con ID: $filmId")

                            // Volver a la pantalla anterior
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

@Composable
fun NavigationGraph(navController: NavHostController, filmViewModel: FilmViewModel) {
    NavHost(navController = navController, startDestination = Routes.FilmListScreen.route) {

        // Pantalla principal de lista de películas
        composable(Routes.FilmListScreen.route) {
            FilmListScreen(navController = navController, viewModel = filmViewModel)
        }

        // Pantalla de detalles de la película
        composable(
            route = Routes.FilmDataScreen.route,
            arguments = listOf(navArgument("filmId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("filmId")?.let { filmId ->
                FilmDataScreen(navController = navController, filmId = filmId, viewModel = filmViewModel)
            } ?: navController.popBackStack()
        }

        // Pantalla de edición de la película
        composable(
            route = Routes.FilmEditScreen.route,
            arguments = listOf(navArgument("filmId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("filmId")?.let { filmId ->
                FilmEditScreen(navController = navController, filmId = filmId, viewModel = filmViewModel)
            } ?: navController.popBackStack()
        }

        // Pantalla "Acerca de"
        composable(Routes.AboutScreen.route) {
            AboutScreen(navController)
        }
    }
}

// Definir un sealed class para manejar rutas de forma más segura
sealed class Routes(val route: String) {
    object FilmListScreen : Routes("filmListScreen")
    object FilmDataScreen : Routes("filmDataScreen/{filmId}")
    object FilmEditScreen : Routes("filmEditScreen/{filmId}")
    object AboutScreen : Routes("aboutScreen")
}

data class Film(
    var id: String? = null,  // ID de Firestore
    var title: String = "",
    var director: String = "",
    var year: Int = 0,
    var genre: Int = 0,
    var format: Int = 0,
    var imdbUrl: String = "",
    var comments: String = "",
    var image: String = ""  // Ahora se almacena en Firestore como un String
) {
    override fun toString(): String {
        return title
    }

    companion object {
        const val FORMAT_DVD = 0
        const val FORMAT_BLURAY = 1
        const val FORMAT_DIGITAL = 2

        const val GENRE_ACTION = 0
        const val GENRE_COMEDY = 1
        const val GENRE_DRAMA = 2
        const val GENRE_SCIFI = 3
        const val GENRE_HORROR = 4

        // Función para obtener el recurso de imagen según el nombre almacenado en Firestore
        fun getImageResource(imageName: String): Int {
            return when (imageName) {
                "harrypotterpiedrafilosofal" -> R.drawable.harrypotterpiedrafilosofal
                "regresoalfuturo" -> R.drawable.regresoalfuturo
                "reyleon" -> R.drawable.reyleon
                else -> R.drawable.defecto
            }
        }
    }

    fun getFormatName(): String {
        return when (format) {
            FORMAT_DVD -> "DVD"
            FORMAT_BLURAY -> "Blu-ray"
            FORMAT_DIGITAL -> "Digital"
            else -> "Desconocido"
        }
    }

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

class FilmRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val filmsCollection = firestore.collection("films")

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> get() = _films

    init {
        filmsCollection.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val filmList = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Film::class.java)?.copy(id = doc.id)
            }
            _films.value = filmList
        }
    }

    suspend fun addFilm(film: Film) {
        filmsCollection.add(film).await()
    }

    suspend fun deleteFilm(id: String) {
        filmsCollection.document(id).delete().await()
    }

    suspend fun updateFilm(film: Film) {
        filmsCollection.document(film.id!!).set(film).await()
    }
}

class FilmViewModel(private val repository: FilmRepository) : ViewModel() {
    val films = repository.films

    fun addFilm(film: Film) {
        viewModelScope.launch { repository.addFilm(film) }
    }

    fun deleteFilm(id: String) {
        viewModelScope.launch { repository.deleteFilm(id) }
    }

    fun updateFilm(film: Film) {
        viewModelScope.launch { repository.updateFilm(film) }
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
