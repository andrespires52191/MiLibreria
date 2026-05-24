package com.example.milibreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.milibreria.adaptador.AdaptadorPrestamos
import com.example.milibreria.databinding.FragmentColeccionPrestamosBinding
import com.example.milibreria.modelo.VM

class ColeccionPrestamosFragment : Fragment() {

    private var _binding: FragmentColeccionPrestamosBinding? = null
    private val binding get() = _binding!!
    private val miViewModel: VM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColeccionPrestamosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_coleccion_prestamos, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.anadir_prestamo_col -> {
                        anadirPrestamo()
                        true
                    }
                    R.id.volver -> {
                        volver()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.rvColeccionPrestamos.layoutManager = LinearLayoutManager(requireContext())

        miViewModel.cargarPrestamos()
        miViewModel.todosLosPrestamos.observe(viewLifecycleOwner) { listaPrestamos ->
            if (listaPrestamos != null) {
                binding.rvColeccionPrestamos.adapter = AdaptadorPrestamos(listaPrestamos) { posicion ->
                    val bundle = bundleOf("posicion" to posicion)
                    findNavController().navigate(
                        R.id.action_coleccionPrestamosFragment_to_anadirPrestamoFragment,
                        bundle
                    )
                }
            }
        }

        binding.btnVolverPrestamos.setOnClickListener {
            volver()
        }

        binding.btnAnadirPrestamoCol.setOnClickListener {
            anadirPrestamo()
        }
    }

    private fun anadirPrestamo() {
        val bundle = bundleOf("posicion" to -1)
        findNavController().navigate(
            R.id.action_coleccionPrestamosFragment_to_anadirPrestamoFragment,
            bundle
        )
    }

    private fun volver() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}