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
        listenToFilms() // 🔥 Escucha cambios en Firestore en tiempo real
        addExampleFilms() // 🔥 Agrega películas de prueba si Firestore está vacío
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

    fun addExampleFilms() {
        viewModelScope.launch {
            val filmsInDB = repository.getFilms() // 🔥 Obtiene las películas actuales en Firestore

            // 🔍 Filtrar solo las películas que NO están en Firestore
            val filmsToAdd = listOf(
                Film(title = "Harry Potter y la Piedra Filosofal", director = "Chris Columbus", year = 2001, genre = "Ciencia Ficción", format = "DVD", comments = "Gran película de magia", image = "harrypotterpiedrafilosofal"),
                Film(title = "Regreso al Futuro", director = "Robert Zemeckis", year = 1985, genre = "Ciencia Ficción", format = "Digital", comments = "Un clásico del viaje en el tiempo", image = "regresoalfuturo"),
                Film(title = "El Rey León", director = "Roger Allers, Rob Minkoff", year = 1994, genre = "Animación", format = "Blu-ray", comments = "Una historia conmovedora", image = "reyleon")
            ).filter { film ->
                // 🔍 Compara el título de cada película con las de Firestore
                filmsInDB.none { it.title == film.title && it.director == film.director }
            }

            if (filmsToAdd.isNotEmpty()) {
                repository.addMultipleFilms(filmsToAdd) // 🔥 Solo inserta las películas que no existen
            }
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