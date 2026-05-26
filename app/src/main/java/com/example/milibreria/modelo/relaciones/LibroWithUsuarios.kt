/* Prueba de relación N:M entre Usuario y Libro */

/*
package com.example.milibreria.modelo.relaciones

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.milibreria.modelo.Libro
import com.example.milibreria.modelo.Usuario

data class LibroWithUsuarios(
    @Embedded val libro: Libro,
    @Relation(
        parentColumn = "ID",
        entityColumn = "ID",
        associateBy = Junction(
            value = PrestamoEntity::class,
            parentColumn = "ID_Libro",
            entityColumn = "ID_Usuario"
        )
    )
    val usuarios: List<Usuario>
)*/
