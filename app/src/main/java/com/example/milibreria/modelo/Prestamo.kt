package com.example.milibreria.modelo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"]
        ),
        ForeignKey(
            entity = Libro::class,
            parentColumns = ["id"],
            childColumns = ["libro_id"]
        )
    ]
)
class Prestamo(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var libro_id: Int,
    var usuario_id: Int,
    var fechaInicio: String,
    var fechaFin: String
)



/* Prueba de relación N:M entre Usuario y Libro */

/*
package com.example.milibreria.modelo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Prestamos",
    foreignKeys = [
        ForeignKey(
            entity = Libro::class,
            parentColumns = ["ID"],
            childColumns = ["ID_Libro"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["ID"],
            childColumns = ["ID_Usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Prestamo(
    @PrimaryKey(autoGenerate = true) val ID: Int = 0,
    val ID_Libro: Int,
    val ID_Usuario: Int,
    val Fecha_Inicio: String, // TEXT (10, 10) para fechas tipo "YYYY-MM-DD"
    val Fecha_Fin: String
)*/
