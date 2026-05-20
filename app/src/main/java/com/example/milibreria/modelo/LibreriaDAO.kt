package com.example.milibreria.modelo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LibreriaDAO {
    @Insert
    fun insertarLibro(ibro: Libro)

    @Insert
    fun insertarUsuario(usuario: Usuario)

    @Update
    fun actualizarLibro(libro: Libro)

    @Query("SELECT * FROM libro  WHERE usuario_id = :usuarioId ORDER BY titulo")
    fun cargarLibros(usuarioId: Int): Flow<List<Libro>>

    @Query("SELECT * FROM usuario WHERE nombre = :usuario AND contrasenia == :contrasenia")
    fun autenticar(usuario: String, contrasenia: String): Flow<Usuario?>
}
