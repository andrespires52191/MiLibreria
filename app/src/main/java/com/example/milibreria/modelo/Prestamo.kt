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