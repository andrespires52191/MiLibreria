package com.example.milibreria.modelo

import com.example.milibreria.modelo.relaciones.PrestamoDetallado
import kotlinx.coroutines.flow.Flow

class Repositorio(val miDao: LibreriaDAO) {

    // === LIBROS ===

    fun insertarLibro(libro: Libro) {
        miDao.insertarLibro(libro)
    }

    fun actualizarLibro(libro: Libro) {
        miDao.actualizarLibro(libro)
    }

    fun cargarLibros(usuarioId: Int): Flow<List<Libro>> {
        return miDao.cargarLibros(usuarioId)
    }

    // === USUARIOS ===

    fun insertarUsuario(usuario: Usuario) {
        miDao.insertarUsuario(usuario)
    }

    fun cargarUsuarios(): Flow<List<Usuario>> = miDao.cargarUsuarios()

    fun actualizarUsuario(usuario: Usuario) = miDao.actualizarUsuario(usuario)

    fun autenticar(usuario: String, contrasenia: String): Flow<Usuario?> {
        return miDao.autenticar(usuario, contrasenia)
    }

    // === PRÉSTAMOS ===

    fun insertarPrestamo(prestamo: Prestamo) = miDao.insertarPrestamo(prestamo)

    fun actualizarPrestamo(prestamo: Prestamo) = miDao.actualizarPrestamo(prestamo)

    fun cargarPrestamos(): Flow<List<PrestamoDetallado>> = miDao.cargarPrestamos()
}
