package com.example.milibreria.modelo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import com.example.milibreria.modelo.relaciones.PrestamoDetallado
import kotlinx.coroutines.flow.Flow

@Dao
interface LibreriaDAO {

    // === LIBROS ===

    @Insert
    fun insertarLibro(libro: Libro)

    @Update
    fun actualizarLibro(libro: Libro)

    @Query("SELECT * FROM libro ORDER BY titulo")
    fun cargarLibros(): Flow<List<Libro>>

    @Delete
    fun eliminarLibro(libro: Libro)

    // === USUARIOS ===

    @Insert
    fun insertarUsuario(usuario: Usuario)

    @Update
    fun actualizarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario ORDER BY nombre")
    fun cargarUsuarios(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuario WHERE nombre = :nombre LIMIT 1")
    fun buscarUsuarioPorNombre(nombre: String): Usuario?

    @Query(
        "SELECT * FROM usuario WHERE usuario = :usuario AND contrasenia = :contrasenia " +
                "AND admin = 1"
    )
    fun autenticar(usuario: String, contrasenia: String): Flow<Usuario?>

    @Delete
    fun eliminarUsuario(usuario: Usuario)

    // === PRÉSTAMOS ===

    @Insert
    fun insertarPrestamo(prestamo: Prestamo)

    @Update
    fun actualizarPrestamo(prestamo: Prestamo)

    @Transaction
    @Query("SELECT * FROM prestamo")
    fun cargarPrestamos(): Flow<List<PrestamoDetallado>>

    @Delete
    fun eliminarPrestamo(prestamo: Prestamo)
}
