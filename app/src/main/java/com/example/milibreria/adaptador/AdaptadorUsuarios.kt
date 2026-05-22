package com.example.milibreria.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.milibreria.databinding.ItemUsuarioBinding
import com.example.milibreria.modelo.Usuario

class AdaptadorUsuarios(val lista: List<Usuario>, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.iutvNombre.text = lista[position].nombre
        holder.binding.root.setOnClickListener { onClick(position) }
    }

    override fun getItemCount(): Int = lista.size

    inner class ViewHolder(val binding: ItemUsuarioBinding) : RecyclerView.ViewHolder(binding.root)
}
