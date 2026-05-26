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



/* Prueba de relación N:M entre Usuario y Libro */

/*
package com.example.milibreria.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Libros",
    indices = [Index(value = ["Titulo"], unique = true)]
)
data class Libro(
    @PrimaryKey(autoGenerate = true) val ID: Int = 0,
    val Titulo: String,
    @ColumnInfo(defaultValue = "Anonimo") val Escritor: String,
    val Ano_Edicion: Int?, // Puede ser NULL
    val Sinopsis: String?, // Puede ser NULL
    @ColumnInfo(defaultValue = "1") val Disponible: Int
)*/