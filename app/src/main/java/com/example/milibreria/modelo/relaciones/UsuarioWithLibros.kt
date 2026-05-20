package com.example.milibreria.modelo.relaciones

import androidx.room.Embedded
import androidx.room.Relation
import com.example.milibreria.modelo.Libro
import com.example.milibreria.modelo.Usuario

data class UsuarioWithLibros(
    @Embedded val usuario: Usuario,
    @Relation(
        parentColumn = "id",
        entityColumn = "usuario_id"
    )
    val libros: List<Libro>
)
