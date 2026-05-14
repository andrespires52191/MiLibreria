package com.example.milibreria.modelo

import androidx.lifecycle.ViewModel

class VM : ViewModel() {
    lateinit var usuarioActual: Usuario

    var usuarios: MutableList<Usuario> = mutableListOf()

    init {
        // Datos de prueba.
        // No intentar crear/guardar datos nuevos aquí.
        // Esperar a los métodos de la base de datos.
        var usuario = Usuario(0, "abcd", "1234")
        var libros = usuario.libros
        libros.add(Libro(0, "titulo_1", "autor_1", "isbn_1", 1999, 4.0))
        libros.add(Libro(1, "titulo_2", "autor_2", "isbn_2", 1999, 4.0))
        libros.add(Libro(2, "titulo_3", "autor_3", "isbn_3", 1999, 4.0))
        usuarios.add(usuario)
    }

    fun autentificar(usuario: String, contrasenia: String): Boolean {
        // Pruebas: Saltar autentificación
        usuarioActual = usuarios.get(0)
        return true
    }

    fun listaLibros(): MutableList<Libro> {
        // Pruebas: Saltar autentificación
        usuarioActual = usuarios.get(0)
        return usuarioActual.libros
    }
}
