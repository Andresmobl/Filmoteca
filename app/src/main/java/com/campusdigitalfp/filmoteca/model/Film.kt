package com.campusdigitalfp.filmoteca.model

data class Film(
    var id: Int = 0,
    var imageResId: Int = 0,
    var title: String? = null,
    var director: String? = null,
    var year: Int = 0,
    var genre: Int = 0,
    var format: Int = 0,
    var imdbUrl: String? = null,
    var comments: String? = null
) {
    override fun toString(): String {
        return title ?: "<Sin título>"
    }

    companion object {
        // Formatos disponibles
        const val FORMAT_DVD = 0
        const val FORMAT_BLURAY = 1
        const val FORMAT_DIGITAL = 2

        // Géneros de películas
        const val GENRE_ACTION = 0
        const val GENRE_COMEDY = 1
        const val GENRE_DRAMA = 2
        const val GENRE_SCIFI = 3
        const val GENRE_HORROR = 4
    }

    // Devuelve el nombre del formato como texto
    fun getFormatName(): String {
        return when (format) {
            FORMAT_DVD -> "DVD"
            FORMAT_BLURAY -> "Blu-ray"
            FORMAT_DIGITAL -> "Digital"
            else -> "Desconocido"
        }
    }

    // Devuelve el nombre del género como texto
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