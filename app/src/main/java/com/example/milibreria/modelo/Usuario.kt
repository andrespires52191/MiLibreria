package com.example.milibreria.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Usuario(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var nombre: String,
    var apellido1: String,
    var apellido2: String,
    var telefono: String,
    var admin: Boolean = false, // Por defecto es un usuario lector normal
    var usuario: String,
    var contrasenia: String
)


/* Prueba de relación N:M entre Usuario y Libro */
/*
package com.example.milibreria.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val ID: Int = 0,
    val Nombre: String,
    val Apellido_1: String,
    val Apellido_2: String?,
    val Telefono: Long
)*/
