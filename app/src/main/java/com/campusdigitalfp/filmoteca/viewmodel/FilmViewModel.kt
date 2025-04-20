package com.campusdigitalfp.filmoteca.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusdigitalfp.filmoteca.model.Film
import com.campusdigitalfp.filmoteca.repository.FilmRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * `FilmViewModel` gestiona la lógica de la aplicación relacionada con las películas.
 * Se comunica con `FilmRepository` para manejar las operaciones en Firestore.
 */
class FilmViewModel : ViewModel() {

    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val repository: FilmRepository = uid?.let { FilmRepository(it) }
        ?: throw IllegalStateException("El usuario no está autenticado")

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> get() = _films

    init {
        listenToFilms()
    }

    private fun listenToFilms() {
        repository.listenToFilmsUpdates { updatedFilms ->
            _films.value = updatedFilms
        }
    }

    private fun fetchFilms() {
        viewModelScope.launch {
            _films.value = repository.getFilms()
        }
    }

    fun addFilm(film: Film) {
        viewModelScope.launch {
            repository.addFilm(film)
            fetchFilms()
        }
    }

    fun updateFilm(film: Film) {
        viewModelScope.launch {
            repository.updateFilm(film)
            fetchFilms()
        }
    }

    fun deleteFilm(filmId: String) {
        viewModelScope.launch {
            repository.deleteFilm(filmId)
            fetchFilms()
        }
    }

    fun deleteSelectedFilms(selectedFilms: List<Film>) {
        viewModelScope.launch {
            repository.deleteMultipleFilms(selectedFilms)
            fetchFilms()
        }
    }
}
