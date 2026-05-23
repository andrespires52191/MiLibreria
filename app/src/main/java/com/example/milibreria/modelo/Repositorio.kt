package com.example.milibreria.modelo

import com.example.milibreria.modelo.relaciones.PrestamoDetallado
import kotlinx.coroutines.flow.Flow

class Repositorio(val miDao: LibreriaDAO) {

    // === LIBROS ===

    // "suspend" permite pausar la función en segundo plano sin congelar la pantalla
    suspend fun insertarLibro(libro: Libro) {
        miDao.insertarLibro(libro)
    }

    suspend fun actualizarLibro(libro: Libro) {
        miDao.actualizarLibro(libro)
    }

    fun cargarLibros(usuarioId: Int): Flow<List<Libro>> {
        return miDao.cargarLibros(usuarioId)
    }

    suspend fun eliminarLibro(libro: Libro) {
        miDao.eliminarLibro(libro)
    }

    // === USUARIOS ===

    suspend fun insertarUsuario(usuario: Usuario) {
        miDao.insertarUsuario(usuario)
    }

    suspend fun actualizarUsuario(usuario: Usuario) = miDao.actualizarUsuario(usuario)

    fun cargarUsuarios(): Flow<List<Usuario>> = miDao.cargarUsuarios()

    suspend fun buscarUsuarioPorNombre(nombre: String): Usuario? {
        return miDao.buscarUsuarioPorNombre(nombre)
    }

    fun autenticar(usuario: String, contrasenia: String): Flow<Usuario?> {
        return miDao.autenticar(usuario, contrasenia)
    }

    // === PRÉSTAMOS ===

    suspend fun insertarPrestamo(prestamo: Prestamo) = miDao.insertarPrestamo(prestamo)

    suspend fun actualizarPrestamo(prestamo: Prestamo) = miDao.actualizarPrestamo(prestamo)

    fun cargarPrestamos(): Flow<List<PrestamoDetallado>> = miDao.cargarPrestamos()
}
