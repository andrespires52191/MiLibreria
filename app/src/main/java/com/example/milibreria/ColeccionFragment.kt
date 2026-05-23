package com.example.milibreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.adaptador.AdaptadorLibros
import com.example.milibreria.databinding.FragmentColeccionBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ColeccionFragment : Fragment() {

    private var _binding: FragmentColeccionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentColeccionBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_coleccion_libros, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.volver -> {
                        findNavController().navigateUp()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.cfrvColeccion.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(activity)

        // Observar los cambios en la lista de libros
        (activity as MainActivity).miViewModel.libros.observe(viewLifecycleOwner) { libros ->
            // Pasar la lista y el bloque de código que se activará al clicar en la papelera
            binding.cfrvColeccion.adapter =
                AdaptadorLibros(libros.toMutableList()) { libroAEliminar ->

                    // Llamar al ViewModel para eliminar el libro en segundo plano con Room
                    (activity as MainActivity).miViewModel.eliminarLibro(libroAEliminar)

                    // Mostrar un aviso rápido al usuario
                    Toast.makeText(
                        requireContext(),
                        "Se ha eliminado: ${libroAEliminar.titulo}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
