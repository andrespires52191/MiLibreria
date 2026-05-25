package com.example.milibreria.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.milibreria.modelo.Libro
import com.example.milibreria.databinding.ItemLibroBinding
import com.example.milibreria.R

// El adapatdor administra los contenedores
class AdaptadorLibros(
    val lista: MutableList<Libro>,
    private val onBorrarClick: (Libro) -> Unit
) :
    RecyclerView.Adapter<AdaptadorLibros.ViewHolder>() {

    // Capturar la vista que hemos creado (recyclerview_item) y crea una instancia del viewholder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLibroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Cargar los datos en cada una de las instancias del ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libroActual = lista[position]

        // Hay que pasar la posición para que el adaptador sepa exactamente qué índice está pulsando el usuario y mostrar los datos del libro seleccionado
        holder.posicion = position
        holder.binding.iltvTitulo.text = libroActual.titulo
        holder.binding.iltvAutor.text = libroActual.autor

        // Programar el clic en la papelera roja
        holder.binding.btnBorrarLibro.setOnClickListener {
            onBorrarClick(libroActual)
        }
    }

    // Retornar el número de elementos que vamos a querer que tenga el contenedor padre
    override fun getItemCount(): Int {
        return lista.count()
    }

    // El ViewHolder es la clase de cada uno de los contenedores
    inner class ViewHolder(val binding: ItemLibroBinding) : RecyclerView.ViewHolder(binding.root) {
        var posicion: Int = 0

        init {
            binding.root.setOnClickListener {
                // Crear el paquete con los datos (la posición del elemento clicado)
                val miBundle = bundleOf("posicion" to posicion)

                // Navegar al fragmento de detalle usando el ID de la acción del nav_graph
                binding.root.findNavController().navigate(
                    R.id.action_coleccionFragment_to_verLibroFragment,
                    miBundle
                )
            }
        }
    }
}
