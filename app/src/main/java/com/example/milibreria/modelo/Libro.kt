package com.example.milibreria.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Libro(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var titulo: String,
    var autor: String?,
    var isbn: String?,
    var publicacion: Int?,
    var valoracion: Double?,
    var usuario_id: Int // FK Usuario (relaciones/UsuarioWithLibros)
) {
}
