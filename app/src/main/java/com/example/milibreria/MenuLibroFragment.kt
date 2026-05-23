package com.example.milibreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentMenuLibroBinding

class MenuLibroFragment : Fragment() {

    private var _binding: FragmentMenuLibroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuLibroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_libro, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_anadir_libro -> {
                        anadirLibro()
                        true
                    }
                    R.id.action_ver_libros -> {
                        verLibros()
                        true
                    }
                    R.id.action_anadir_usuario -> {
                        anadirUsuario()
                        true
                    }
                    R.id.action_ver_usuarios -> {
                        verUsuarios()
                        true
                    }
                    R.id.action_anadir_prestamo -> {
                        anadirPrestamo()
                        true
                    }
                    R.id.action_ver_prestamos -> {
                        verPrestamos()
                        true
                    }
                    R.id.volver -> {
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // === ACCIONES DE LIBROS ===

        binding.btnHomeVer.setOnClickListener {
            verLibros()
        }

        binding.btnHomeAnadir.setOnClickListener {
            anadirLibro()
        }

        // === ACCIONES DE USUARIOS ===

        binding.btnMenuVerUsuarios.setOnClickListener {
            verUsuarios()
        }

        binding.btnMenuAnadirUsuario.setOnClickListener {
            anadirUsuario()
        }

        // === ACCIONES DE PRÉSTAMOS ===

        binding.btnMenuVerPrestamos.setOnClickListener {
            verPrestamos()
        }

        binding.btnMenuAnadirPrestamo.setOnClickListener {
            anadirPrestamo()
        }
    }

    private fun anadirLibro() {
        // Se le pasa un "-1" para indicar que es un libro nuevo
        val miBundle = bundleOf("posicion" to -1)
        findNavController().navigate(R.id.action_homeFragment_to_detalleLibroFragment, miBundle)
    }

    private fun verLibros() {
        findNavController().navigate(R.id.action_menuLibroFragment_to_coleccionFragment)
    }

    private fun anadirUsuario() {
        val miBundle = bundleOf("posicion" to -1)
        findNavController().navigate(R.id.action_menuLibroFragment_to_anadirUsuarioFragment, miBundle)
    }

    private fun verUsuarios() {
        findNavController().navigate(R.id.action_menuLibroFragment_to_coleccionUsuariosFragment)
    }

    private fun anadirPrestamo() {
        val miBundle = bundleOf("posicion" to -1)
        findNavController().navigate(R.id.action_menuLibroFragment_to_anadirPrestamoFragment, miBundle)
    }

    private fun verPrestamos() {
        findNavController().navigate(R.id.action_menuLibroFragment_to_coleccionPrestamosFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}