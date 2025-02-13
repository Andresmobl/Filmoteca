package com.campusdigitalfp.filmoteca.repository

import android.util.Log
import com.campusdigitalfp.filmoteca.model.Film
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * `FilmRepository` maneja todas las operaciones relacionadas con las películas en Firestore.
 * Actúa como una capa intermedia entre Firestore y el ViewModel.
 */
class FilmRepository {

    // Obtiene una instancia de Firestore.
    private val db = FirebaseFirestore.getInstance()

    // Referencia a la colección "films" en Firestore donde se almacenan las películas.
    private val filmsCollection = db.collection("films")

    /**
     * Agrega una nueva película a Firestore.
     */
    suspend fun addFilm(film: Film) {
        filmsCollection.add(film).await()
    }

    /**
     * Obtiene todas las películas desde Firestore como una lista.
     * Si ocurre un error, devuelve una lista vacía para evitar fallos en la UI.
     */
    suspend fun getFilms(): List<Film> {
        return try {
            val snapshot = filmsCollection.get().await() // Obtiene los documentos
            snapshot.documents.mapNotNull { it.toObject(Film::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            Log.e("FilmRepository", "Error al obtener películas: ${e.message}")
            emptyList()
        }
    }

    /**
     * Actualiza una película en Firestore usando su ID.
     */
    suspend fun updateFilm(film: Film) {
        film.id.let { id ->
            filmsCollection.document(id).set(film).await()
        }
    }

    /**
     * Elimina una película específica de Firestore usando su ID.
     */
    suspend fun deleteFilm(filmId: String) {
        filmsCollection.document(filmId).delete().await()
    }

    /**
     * Escucha cambios en tiempo real en la colección de películas.
     * Cada vez que Firestore detecta un cambio, se actualiza la lista de películas.
     */
    fun listenToFilmsUpdates(onUpdate: (List<Film>) -> Unit) {
        filmsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("FilmRepository", "Error al escuchar películas: ${exception.message}")
                return@addSnapshotListener
            }

            val films = snapshot?.documents?.mapNotNull { it.toObject(Film::class.java)?.copy(id = it.id) } ?: emptyList()
            onUpdate(films) // Llama al callback con la lista actualizada
        }
    }

    /**
     * Agrega múltiples películas en una sola operación usando `WriteBatch` para optimizar rendimiento.
     */
    suspend fun addMultipleFilms(films: List<Film>) {
        val batch = db.batch()

        films.forEach { film ->
            val newDocRef = filmsCollection.document()
            batch.set(newDocRef, film.copy(id = newDocRef.id))
        }

        try {
            batch.commit().await()
            Log.i("FilmRepository", "Películas añadidas correctamente a Firestore")
        } catch (e: Exception) {
            Log.e("FilmRepository", "Error al añadir películas: ${e.message}")
        }
    }

    /**
     * Elimina múltiples películas en una sola transacción usando `WriteBatch` para eficiencia.
     */
    suspend fun deleteMultipleFilms(films: List<Film>) {
        val batch = db.batch()

        films.forEach { film ->
            film.id.let { filmId ->
                batch.delete(filmsCollection.document(filmId))
            }
        }

        try {
            batch.commit().await()
            Log.i("FilmRepository", "Películas eliminadas correctamente de Firestore")
        } catch (e: Exception) {
            Log.e("FilmRepository", "Error al eliminar películas: ${e.message}")
        }
    }
}