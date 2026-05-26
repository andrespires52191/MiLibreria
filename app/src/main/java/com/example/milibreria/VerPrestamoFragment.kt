package com.example.milibreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentVerPrestamoBinding
import com.example.milibreria.modelo.VM

class VerPrestamoFragment : Fragment() {

    private var _binding: FragmentVerPrestamoBinding? = null
    private val binding get() = _binding!!

    private val miViewModel: VM by activityViewModels()
    private var posicion: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerPrestamoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_ver_prestamo, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_editar -> {
                        editar()
                        true
                    }

                    R.id.action_volver -> {
                        volver()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        posicion = arguments?.getInt("posicion") ?: -1

        if (posicion != -1) {
            // Observar la lista en tiempo real para enterarnos de los cambios al volver de editar
            miViewModel.todosLosPrestamos.observe(viewLifecycleOwner) { listaPrestamos ->
                val prestamoDetalle = listaPrestamos?.getOrNull(posicion)
                prestamoDetalle?.let {
                    binding.etVerPrestamoLibro.setText(it.libro?.titulo ?: "Desconocido")
                    binding.etVerPrestamoUsuario.setText(it.usuario?.nombre ?: "Desconocido")
                    binding.etVerFechaInicio.setText(it.prestamo.fechaInicio)
                    binding.etVerFechaFin.setText(it.prestamo.fechaFin)
                }
            }
        }

        binding.btnVolverPrestamo.setOnClickListener {
            volver()
        }

        binding.btnEditarPrestamo.setOnClickListener {
            editar()
        }
    }

    private fun editar() {
        val miBundle = bundleOf("posicion" to posicion)
        findNavController().navigate(
            R.id.action_verPrestamoFragment_to_anadirPrestamoFragment,
            miBundle
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