package com.example.milibreria.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.milibreria.R
import com.example.milibreria.databinding.ItemPrestamoBinding
import com.example.milibreria.modelo.Usuario

class AdaptadorPrestamos(val lista: List<Usuario>, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<AdaptadorPrestamos.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPrestamoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.iptvLibroTitulo.text = item.libro.titulo
        holder.binding.iptvUsuarioNombre.text = item.usuario.nombre
        // TODO : Poner lo de fechas
        //holder.binding.iptvFechas.text = "${item.prestamo.fechaInicio} hasta ${item.prestamo.fechaFin}"
        holder.binding.root.setOnClickListener { onClick(position) }
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

    inner class ViewHolder(val binding: ItemPrestamoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var posicion: Int = 0

        init {
            binding.root.setOnClickListener {
                val miBundle = bundleOf("posicion" to posicion)

                binding.root.findNavController().navigate(
                    R.id.action_coleccionFragment_to_anadirPrestamoFragment,
                    miBundle
                )
            }
        }
    }
}
