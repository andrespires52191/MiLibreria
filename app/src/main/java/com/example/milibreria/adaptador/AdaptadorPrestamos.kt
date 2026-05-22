package com.example.milibreria.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.milibreria.databinding.ItemPrestamoBinding
import com.example.milibreria.modelo.relaciones.PrestamoDetallado

class AdaptadorPrestamos(val lista: List<PrestamoDetallado>, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<AdaptadorPrestamos.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPrestamoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.iptvLibroTitulo.text = item.libro.titulo
        holder.binding.iptvUsuarioNombre.text = item.usuario.nombre
        holder.binding.iptvFechas.text = "${item.prestamo.fechaInicio} - ${item.prestamo.fechaFin}"
        holder.binding.root.setOnClickListener { onClick(position) }
    }

    override fun getItemCount(): Int = lista.size

    inner class ViewHolder(val binding: ItemPrestamoBinding) : RecyclerView.ViewHolder(binding.root)
}
