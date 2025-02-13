package com.campusdigitalfp.filmoteca.model

import com.google.firebase.firestore.DocumentId

data class Film(
    @DocumentId val id: String = "", // Firestore asignará un ID único automáticamente
    val title: String = "",
    val director: String = "",
    val year: Int = 0,
    val genre: String = "",
    val format: String = "",
    val imdbUrl: String = "",
    val comments: String = "",
    val image: String = "default_image" // Nombre del recurso drawable
) {
    override fun toString(): String {
        return title.ifBlank { "<Sin título>" }
    }

    // 🔥 Devuelve el nombre del formato
    fun getFormatName(): String {
        return when (format) {
            FORMAT_DVD -> "DVD"
            FORMAT_BLURAY -> "Blu-ray"
            FORMAT_DIGITAL -> "Digital"
            else -> "Formato desconocido"
        }
    }

    // 🔥 Devuelve el nombre del género
    fun getGenreName(): String {
        return when (genre) {
            GENRE_ACTION -> "Acción"
            GENRE_COMEDY -> "Comedia"
            GENRE_DRAMA -> "Drama"
            GENRE_SCIFI -> "Ciencia Ficción"
            GENRE_HORROR -> "Terror"
            else -> "Género desconocido"
        }
    }

    companion object {
        // Lista de formatos disponibles
        const val FORMAT_DVD = "DVD"
        const val FORMAT_BLURAY = "Blu-ray"
        const val FORMAT_DIGITAL = "Digital"

        // Lista de géneros
        const val GENRE_ACTION = "Acción"
        const val GENRE_COMEDY = "Comedia"
        const val GENRE_DRAMA = "Drama"
        const val GENRE_SCIFI = "Ciencia Ficción"
        const val GENRE_HORROR = "Terror"

        // Método para obtener la imagen desde el nombre
        fun getImageResource(imageName: String): Int {
            return when (imageName) {
                "harrypotterpiedrafilosofal" -> com.campusdigitalfp.filmoteca.R.drawable.harrypotterpiedrafilosofal
                "regresoalfuturo" -> com.campusdigitalfp.filmoteca.R.drawable.regresoalfuturo
                "reyleon" -> com.campusdigitalfp.filmoteca.R.drawable.reyleon
                else -> com.campusdigitalfp.filmoteca.R.drawable.defecto
            }
        }
    }
}