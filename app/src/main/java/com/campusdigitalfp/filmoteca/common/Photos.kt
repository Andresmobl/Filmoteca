package com.campusdigitalfp.filmoteca.common

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun saveImageToAppFolder(context: Context, imageUri: Uri): Uri? {
    // Genera un timestamp para darle un nombre único a la imagen
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val directory = File(context.filesDir, "MyAppImages")

    // Verifica si el directorio existe, si no, intenta crearlo
    if (!directory.exists() && !directory.mkdirs()) {
        Log.e("saveImageToAppFolder", "No se pudo crear el directorio")
        return null
    }

    // Crea un archivo en el directorio con el nombre generado
    val file = File(directory, "IMG_$timeStamp.jpg")

    return try {
        // Abre el flujo de entrada desde la URI de la imagen proporcionada
        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            // Crea un flujo de salida hacia el archivo destino
            FileOutputStream(file).use { outputStream ->
                // Copia los datos desde el inputStream al outputStream
                inputStream.copyTo(outputStream)
            }
        }
        // Devuelve la URI del archivo guardado
        Uri.fromFile(file)
    } catch (e: IOException) {
        Log.e("saveImageToAppFolder", "Error al guardar la imagen", e)
        null // En caso de error, devuelve null
    }
}