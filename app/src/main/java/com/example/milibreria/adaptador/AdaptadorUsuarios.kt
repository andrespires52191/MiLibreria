package com.example.milibreria.adaptador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.milibreria.databinding.ItemUsuarioBinding
import com.example.milibreria.modelo.Usuario
import com.example.milibreria.R

class AdaptadorUsuarios(
    val lista: MutableList<Usuario>,
    private val onBorrarClick: (Usuario) -> Unit
) :
    RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuarioActual = lista[position]
        holder.posicion = position
        holder.binding.iutvNombre.text = usuarioActual.nombre
        holder.binding.iutvContrasenia.text = usuarioActual.contrasenia

        // Clic en la papelera de usuarios
        holder.binding.btnBorrarUsuario.setOnClickListener {
            onBorrarClick(usuarioActual)
        }
    }

    override fun getItemCount(): Int = lista.count()

    inner class ViewHolder(val binding: ItemUsuarioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var posicion: Int = 0

        init {
            binding.root.setOnClickListener {
                val miBundle = Bundle().apply { putInt("posicion", posicion) }
                binding.root.findNavController().navigate(
                    R.id.action_coleccionUsuariosFragment_to_anadirUsuarioFragment,
                    miBundle
                )
            }
        }
    }
}
