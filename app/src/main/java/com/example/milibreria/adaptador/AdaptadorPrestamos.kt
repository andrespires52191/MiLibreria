package com.example.milibreria.adaptador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.milibreria.databinding.ItemPrestamoBinding
import com.example.milibreria.modelo.Prestamo
import com.example.milibreria.modelo.relaciones.PrestamoDetallado
import com.example.milibreria.R

class AdaptadorPrestamos(
    val lista: MutableList<PrestamoDetallado>,
    private val onBorrarClick: (Prestamo) -> Unit
) :
    RecyclerView.Adapter<AdaptadorPrestamos.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPrestamoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.posicion = position

        holder.binding.iptvLibroTitulo.text = item.libro.titulo
        holder.binding.iptvUsuarioNombre.text = item.usuario.nombre
        holder.binding.iptvFechas.text = "${item.prestamo.fechaInicio} - ${item.prestamo.fechaFin}"

        // Evento para el botón de la papelera
        holder.binding.btnBorrarPrestamo.setOnClickListener {
            onBorrarClick(item.prestamo)
        }
    }

    override fun getItemCount(): Int = lista.count()

    inner class ViewHolder(val binding: ItemPrestamoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var posicion: Int = 0

        init {
            binding.root.setOnClickListener {
                val miBundle = Bundle().apply { putInt("posicion", posicion) }
                binding.root.findNavController().navigate(
                    R.id.action_coleccionPrestamosFragment_to_verPrestamoFragment,
                    miBundle
                )
            }
        }
    }
}
