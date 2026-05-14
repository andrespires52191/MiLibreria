package com.example.milibreria.modelo

class Usuario(
    var id: Int,
    var nombre: String,
    var contrasenia: String,
) {
    var libros: MutableList<Libro> = mutableListOf()
}
