package com.example.milibreria.modelo

import kotlinx.coroutines.flow.Flow

class Repositorio(val miDao: LibreriaDAO) {
    fun insertarUsuario(usuario: Usuario) {
        miDao.insertarUsuario(usuario)
    }

    fun insertarLibro(libro: Libro) {
        miDao.insertarLibro(libro)
    }

    fun actualizarLibro(libro: Libro) {
        miDao.actualizarLibro(libro)
    }

    fun autenticar(usuario: String, contrasenia: String): Flow<Usuario?> {
        return miDao.autenticar(usuario, contrasenia)
    }

    fun cargarLibros(usuarioId: Int): Flow<List<Libro>> {
        return miDao.cargarLibros(usuarioId)
    }
}
