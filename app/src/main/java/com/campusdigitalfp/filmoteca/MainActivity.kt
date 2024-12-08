package com.campusdigitalfp.filmoteca

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AboutScreen()
        }
    }
}

@Composable
fun AboutScreen() {
    val  context = LocalContext.current // Contexto para mostrar el Toast

    // Función para mostrar el Toast
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Layout de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texto con nombre de autor
        Text(
            text = "Creado por Andres Moreno",
            style = TextStyle(fontSize = 24.sp)
        )
        Spacer(modifier = Modifier.height(10.dp)) // Espaciado entre texto e imagen
        // Mostrar imagen
        val image = painterResource(id = R.drawable.perfil)
        Image(painter = image, contentDescription = "Icono Perfil", modifier = Modifier.size(150.dp))
        Spacer(modifier = Modifier.height(20.dp)) // Espaciado entre imagen y botones

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), //Espaciado entre botonos
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Boton Ir al sitio Web
            Button(onClick = { showToast("Funcionalidad sin implementar") },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Ir al sitio web")
            }
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre botones
            // Boton "Obtener soporte"
            Button(onClick = { showToast("Funcionalidad sin implementar") },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Obtener soporte")
            }
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre las filas de botones
            }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Boton "Volver"
            Button(onClick = { showToast("Funcionalidad sin implementar") }) {
                Text(text = "Volver")
        }
        }
    }
}