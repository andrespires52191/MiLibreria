/* Prueba de relación N:M entre Usuario y Libro */

// Antes
/*package com.example.milibreria.modelo.relaciones

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
)*/



// Actualizado
/*
package com.example.milibreria.modelo.relaciones

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.milibreria.modelo.Libro
import com.example.milibreria.modelo.Usuario

data class UsuarioWithLibros(
    @Embedded val usuario: Usuario,
    @Relation(
        parentColumn = "ID",
        entityColumn = "ID",
        associateBy = Junction(
            value = PrestamoEntity::class,
            parentColumn = "ID_Usuario",
            entityColumn = "ID_Libro"
        )
    )
    val libros: List<Libro>
)*/
