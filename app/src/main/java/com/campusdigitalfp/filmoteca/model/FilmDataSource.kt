package com.campusdigitalfp.filmoteca.model

import com.campusdigitalfp.filmoteca.R

object FilmDataSource {
    val films: MutableList<Film> = mutableListOf()

    init {
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
            id = (films.maxOfOrNull { it.id } ?: 0) + 1,
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
