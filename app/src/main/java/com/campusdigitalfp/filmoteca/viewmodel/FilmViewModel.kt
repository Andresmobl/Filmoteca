package com.campusdigitalfp.filmoteca.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusdigitalfp.filmoteca.model.Film
import com.campusdigitalfp.filmoteca.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * `FilmViewModel` gestiona la lógica de la aplicación relacionada con las películas.
 * Se comunica con `FilmRepository` para manejar las operaciones en Firestore.
 */
class FilmViewModel : ViewModel() {

    // Instancia del repositorio para manejar las operaciones de Firestore.
    private val repository = FilmRepository()

    // `_films` almacena la lista de películas internamente y permite modificaciones.
    private val _films = MutableStateFlow<List<Film>>(emptyList())

    // `films` expone las películas a la UI sin permitir modificaciones directas.
    val films: StateFlow<List<Film>> get() = _films

    // Se ejecuta al inicializar el ViewModel y activa la escucha de cambios en Firestore.
    init {
        listenToFilms()
    }

    /**
     * Escucha los cambios en Firestore en tiempo real.
     * Cuando se detecta un cambio en la colección de películas, la UI se actualiza automáticamente.
     */
    private fun listenToFilms() {
        repository.listenToFilmsUpdates { updatedFilms ->
            _films.value = updatedFilms
        }
    }

    /**
     * Recupera la lista de películas desde Firestore y actualiza `_films`.
     * Se ejecuta dentro de `viewModelScope.launch` para evitar bloquear la UI.
     */
    private fun fetchFilms() {
        viewModelScope.launch {
            _films.value = repository.getFilms()
        }
    }

    /**
     * Agrega una nueva película a Firestore y actualiza la lista de películas.
     */
    fun addFilm(film: Film) {
        viewModelScope.launch {
            repository.addFilm(film)
            fetchFilms() // Recarga la lista después de añadir una nueva película.
        }
    }

    /**
     * Actualiza una película en Firestore y recarga la lista de películas.
     */
    fun updateFilm(film: Film) {
        viewModelScope.launch {
            repository.updateFilm(film)
            fetchFilms()
        }
    }

    /**
     * Elimina una película de Firestore según su ID y actualiza la lista.
     */
    fun deleteFilm(filmId: String) {
        viewModelScope.launch {
            repository.deleteFilm(filmId)
            fetchFilms()
        }
    }

    /**
     * Agrega una lista de películas de ejemplo a Firestore.
     * Se usa para pruebas o para pre-cargar Firestore con datos iniciales.
     */
    fun addExampleFilms() {
        val films = listOf(
            Film(title = "Harry Potter y la Piedra Filosofal", director = "Chris Columbus", year = 2001, genre = "Fantasía", format = "DVD", comments = "Gran película de magia", image = "harry_potter"),
            Film(title = "Regreso al Futuro", director = "Robert Zemeckis", year = 1985, genre = "Ciencia Ficción", format = "Digital", comments = "Un clásico del viaje en el tiempo", image = "back_to_future"),
            Film(title = "El Rey León", director = "Roger Allers, Rob Minkoff", year = 1994, genre = "Animación", format = "Blu-ray", comments = "Una historia conmovedora", image = "lion_king"),
            Film(title = "Titanic", director = "James Cameron", year = 1997, genre = "Drama", format = "DVD", comments = "Un clásico romántico", image = "titanic"),
            Film(title = "El Señor de los Anillos: La Comunidad del Anillo", director = "Peter Jackson", year = 2001, genre = "Fantasía", format = "Blu-ray", comments = "Inicio de una gran saga", image = "lotr"),
            Film(title = "Matrix", director = "Lana y Lilly Wachowski", year = 1999, genre = "Ciencia Ficción", format = "Digital", comments = "Revolucionó el cine de acción", image = "matrix"),
            Film(title = "Avengers: Endgame", director = "Anthony y Joe Russo", year = 2019, genre = "Acción", format = "Digital", comments = "Épico cierre de una saga", image = "avengers_endgame"),
            Film(title = "Gladiador", director = "Ridley Scott", year = 2000, genre = "Drama", format = "DVD", comments = "Película inspiradora", image = "gladiator"),
            Film(title = "Joker", director = "Todd Phillips", year = 2019, genre = "Drama", format = "Blu-ray", comments = "Un papel espectacular de Joaquin Phoenix", image = "joker"),
            Film(title = "Interstellar", director = "Christopher Nolan", year = 2014, genre = "Ciencia Ficción", format = "Blu-ray", comments = "Ciencia y emociones en el espacio", image = "interstellar")
        )

        viewModelScope.launch {
            repository.addMultipleFilms(films) // Inserta las películas en una sola operación.
        }
    }

    /**
     * Elimina múltiples películas seleccionadas en una sola operación en Firestore.
     * Luego, actualiza la lista de películas en la UI.
     */
    fun deleteSelectedFilms(selectedFilms: List<Film>) {
        viewModelScope.launch {
            repository.deleteMultipleFilms(selectedFilms)
            fetchFilms() // Recarga la lista tras la eliminación.
        }
    }
}