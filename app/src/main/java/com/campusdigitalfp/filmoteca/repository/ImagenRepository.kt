package com.campusdigitalfp.filmoteca.repository

import android.util.Log
import com.campusdigitalfp.filmoteca.model.Imagen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ImagenRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Obtiene la referencia a la subcolección "imagenes" dentro del usuario autenticado
    private fun getUserImagesCollection() =
        auth.currentUser?.let { user ->
            db.collection("usuarios").document(user.uid).collection("imagenes")
        }

    /**
     * Agrega una imagen a la subcolección del usuario autenticado.
     */
    suspend fun addImagen(imagen: Imagen) {
        getUserImagesCollection()?.add(imagen)?.await()
    }

    /**
     * Obtiene todas las imágenes almacenadas en la subcolección del usuario autenticado.
     */
    suspend fun getImagenes(): List<Imagen> {
        return try {
            getUserImagesCollection()?.get()?.await()?.documents?.mapNotNull {
                it.toObject(Imagen::class.java)?.copy(id = it.id)
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Elimina una imagen específica del usuario autenticado.
     */
    suspend fun deleteImagen(imagenId: String) {
        getUserImagesCollection()?.document(imagenId)?.delete()?.await()
    }

    /**
     * Elimina todas las imágenes relacionadas con un hábito específico en la colección del usuario.
     */
    suspend fun deleteImagenesHabitos(habitoId: String) {
        try {
            val snapshot = getUserImagesCollection()?.whereEqualTo("habito", habitoId)?.get()?.await()
            snapshot?.documents?.forEach { document ->
                getUserImagesCollection()?.document(document.id)?.delete()?.await()
            }
            Log.i("HS_info", "Todas las imágenes del hábito $habitoId han sido eliminadas.")
        } catch (e: Exception) {
            Log.e("HS_error", "Error eliminando imágenes: ${e.message}")
        }
    }

    /**
     * Escucha cambios en la subcolección de imágenes del usuario autenticado en Firestore.
     */
    fun listenToImagenesUpdates(onUpdate: (List<Imagen>) -> Unit) {
        getUserImagesCollection()?.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("HS_error", "Error al obtener imágenes: ${exception.message}")
                return@addSnapshotListener
            }

            val imagenes = snapshot?.documents?.mapNotNull {
                it.toObject(Imagen::class.java)?.copy(id = it.id)
            } ?: emptyList()
            onUpdate(imagenes)
        }
    }
}