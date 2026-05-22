package com.example.milibreria.modelo.relaciones

import androidx.room.Embedded
import androidx.room.Relation
import com.example.milibreria.modelo.Libro
import com.example.milibreria.modelo.Prestamo
import com.example.milibreria.modelo.Usuario

data class PrestamoDetallado(
    @Embedded val prestamo: Prestamo,
    @Relation(parentColumn = "libro_id", entityColumn = "id")
    val libro: Libro,
    @Relation(parentColumn = "usuario_id", entityColumn = "id")
    val usuario: Usuario
)