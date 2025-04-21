package com.campusdigitalfp.filmoteca.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageCarousel(imageUrls: List<String>) {
    // Estado del paginador, maneja el número total de páginas basándose en la cantidad de imágenes
    val pagerState = rememberPagerState (pageCount = { imageUrls.size })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Contenedor horizontal que permite el desplazamiento entre imágenes
        HorizontalPager(state = pagerState) { page ->
            Image(
                // Carga y muestra la imagen de forma asíncrona desde la URL proporcionada
                painter = rememberAsyncImagePainter(imageUrls[page]),
                contentDescription = "Imagen $page", // Descripción accesible de la imagen
                modifier = Modifier
                    .fillMaxWidth() // Ajusta el ancho de la imagen al tamaño del contenedor
                    .height(300.dp) // Define una altura fija para todas las imágenes
            )
        }
    }
}