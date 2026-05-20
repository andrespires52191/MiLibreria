package com.example.milibreria.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Usuario(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var nombre: String,
    var contrasenia: String,
) {
}
