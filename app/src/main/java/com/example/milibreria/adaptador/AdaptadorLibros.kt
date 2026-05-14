package com.example.milibreria.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.milibreria.modelo.Libro
import com.example.milibreria.databinding.ItemLibroBinding

//El adapatdor administra los contenedores
class AdaptadorLibros(val lista: MutableList<Libro>) :
    RecyclerView.Adapter<AdaptadorLibros.ViewHolder>() {

    //captura la vista que hemos creado (recyclerview_item) y crea una instancia del viewholder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLibroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //cargamos los datos en cada una de las instancias del ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.posicion = position
        holder.binding.iltvTitulo.text = lista[position].titulo
        holder.binding.iltvAutor.text = lista[position].autor
    }

    //retorna el número de elementos que vamos a querer que tenga el contenedor padre
    override fun getItemCount(): Int {
        return lista.count()
    }

    //El ViewHolder es la clase de cada uno de los contenedores
    inner class ViewHolder(val binding: ItemLibroBinding) : RecyclerView.ViewHolder(binding.root) {
        var posicion: Int = 0

        init {
            binding.root.setOnClickListener {
//                val miBundle = bundleOf("posicion" to posicion)
//                binding.rviClPrincipal.findNavController()
//                    .navigate(R.id.action_thirdFragment_to_fourthFragment, miBundle)
            }
        }
    }
}
