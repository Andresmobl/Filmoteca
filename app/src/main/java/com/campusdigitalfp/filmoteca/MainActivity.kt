package com.campusdigitalfp.filmoteca

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.campusdigitalfp.filmoteca.navigation.NavigationGraph
import com.campusdigitalfp.filmoteca.ui.theme.FilmotecaTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = FirebaseFirestore.getInstance() // Inicializa Firestore

        setContent {
            FilmotecaTheme {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }
}

