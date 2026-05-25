package com.example.milibreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentVerLibroBinding
import com.example.milibreria.modelo.VM

class VerLibroFragment : Fragment() {

    private var _binding: FragmentVerLibroBinding? = null
    private val binding get() = _binding!!

    private val miViewModel: VM by activityViewModels()
    private var posicion: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerLibroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_ver_libro, menu)
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
            val libro = miViewModel.getLibro(posicion)
            libro?.let {
                binding.etVerTitulo.setText(it.titulo)
                binding.etVerAutor.setText(it.autor)
                binding.etVerIsbn.setText(it.isbn)
                binding.etVerPublicacion.setText(it.publicacion?.toString() ?: "")
                binding.etVerValoracion.setText(it.valoracion?.toString() ?: "")
            }
        }

        binding.btnVolverLibro.setOnClickListener {
            volver()
        }

        binding.btnEditarLibro.setOnClickListener {
            editar()
        }
    }

    private fun editar() {
        val miBundle = bundleOf("posicion" to posicion)
        findNavController().navigate(R.id.action_verLibroFragment_to_anadirLibroFragment, miBundle)
    }

    private fun volver() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
