package com.example.milibreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

        // === ACCIONES DE LIBROS ===

        binding.btnHomeVer.setOnClickListener {
            findNavController().navigate(R.id.action_menuLibroFragment_to_coleccionFragment)
        }

        // Se le pasa un "-1" para indicar que es un libro nuevo
        binding.btnHomeAnadir.setOnClickListener {
            val miBundle = bundleOf("posicion" to -1)
            findNavController().navigate(R.id.action_homeFragment_to_detalleLibroFragment, miBundle)
        }

        // === ACCIONES DE USUARIOS ===

        binding.btnMenuVerUsuarios.setOnClickListener {
            findNavController().navigate(R.id.action_menuLibroFragment_to_coleccionUsuariosFragment)
        }

        binding.btnMenuAnadirUsuario.setOnClickListener {
            val miBundle = bundleOf("posicion" to -1)
            findNavController().navigate(R.id.action_menuLibroFragment_to_anadirUsuarioFragment, miBundle)
        }

        // === ACCIONES DE PRÉSTAMOS ===

        binding.btnMenuVerPrestamos.setOnClickListener {
            findNavController().navigate(R.id.action_menuLibroFragment_to_coleccionPrestamosFragment)
        }

        binding.btnMenuAnadirPrestamo.setOnClickListener {
            val miBundle = bundleOf("posicion" to -1)
            findNavController().navigate(R.id.action_menuLibroFragment_to_anadirPrestamoFragment, miBundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}