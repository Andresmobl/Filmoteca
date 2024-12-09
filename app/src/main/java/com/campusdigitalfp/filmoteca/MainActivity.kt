package com.campusdigitalfp.filmoteca

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campusdigitalfp.filmoteca.R.string.back_button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AboutScreen()
        }
    }
}

// Función para mostrar un Toast, definida fuera del composable
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Función para abrir una página web
fun abrirPaginaWeb(url: String, context: Context){
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://www.instagram.com/andresmor96/") // Establecer la URL
    }
    context.startActivity(intent) // Inicia la actividad
}

// Función para mandar un correo electrónico
fun mandarEmail(context: Context, email: String, asunto: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email") // Establece el destinatario del email
        putExtra(Intent.EXTRA_SUBJECT, asunto) // Establece el asunto
    }

    // Verifica si hay una aplicación que pueda manejar el Intent
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent) // Inicia la actividad si hay un manejador
    } else {
        showToast(context, "No se encontró una aplicación de correo electrónico.")
    }
}

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    // Resolvemos los textos fuera de showToast
    val aboutAuthorText = stringResource(id = R.string.about_author)
    val webButtonText = stringResource(id = R.string.web_button)
    val supportButtonText = stringResource(id = R.string.support_button)
    val backButtonText = stringResource(id = back_button)
    val toastMessage = stringResource(id = R.string.toast_functionality_not_implemented)
    val emailSupportSubject = stringResource(id = R.string.incidence_with_film_library) // Asunto del email
    val supportEmail = "andresmorenoblanc1996@gmail.com" // Correo de soporte

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
            text = aboutAuthorText,
            style = TextStyle(fontSize = 24.sp)
        )
        Spacer(modifier = Modifier.height(10.dp)) // Espaciado entre texto e imagen
        // Mostrar imagen
        val image = painterResource(id = R.drawable.perfil)
        Image(
            painter = image,
            contentDescription = "Icono Perfil",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(20.dp)) // Espaciado entre imagen y botones

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), //Espaciado entre botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Boton Ir al sitio Web
            Button(
                onClick = {
                    // Abre la página web cuando se presiona el botón
                    abrirPaginaWeb("https://www.google.es", context) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = webButtonText)
            }
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre botones
            // Boton "Obtener soporte"
            Button(
                onClick = {
                    // Abre la aplicación de correo con el asunto predefinido
                    mandarEmail(context = context, email = supportEmail, asunto = emailSupportSubject) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = supportButtonText)
            }
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre las filas de botones
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Boton "Volver"
            Button(onClick = {
                showToast(context = context, message = toastMessage) }) {
                Text(text = backButtonText)
            }
        }
    }
}
