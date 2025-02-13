package com.campusdigitalfp.filmoteca

import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.campusdigitalfp.filmoteca.navigation.NavigationGraph
import com.campusdigitalfp.filmoteca.ui.theme.FilmotecaTheme
import com.campusdigitalfp.filmoteca.viewmodel.FilmViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Inicializa Firestore para la aplicación.
        val db = FirebaseFirestore.getInstance()

        // 🔥 Configuración de Firestore (sin almacenamiento en caché persistente).
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(
                MemoryCacheSettings.newBuilder().build() // Usa solo RAM, sin guardar en disco.
            )
            .build()

        db.firestoreSettings = settings // Aplica la configuración en Firestore.

        enableEdgeToEdge()
        setContent {
            FilmotecaTheme {
                // 🔥 Instancia centralizada de `FilmViewModel`
                val filmViewModel: FilmViewModel = viewModel()

                // 🔥 Configura la navegación y pasa el `ViewModel`
                val navController = rememberNavController()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    NavigationGraph(navController = navController, filmViewModel = filmViewModel)
                }
            }
        }
    }
}

