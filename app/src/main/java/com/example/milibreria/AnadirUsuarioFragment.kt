package com.example.milibreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentAnadirUsuarioBinding
import com.example.milibreria.modelo.Usuario
import com.example.milibreria.modelo.VM

class AnadirUsuarioFragment : Fragment() {

    private var _binding: FragmentAnadirUsuarioBinding? = null
    private val binding get() = _binding!!
    private val miViewModel: VM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnadirUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_anadir_usuario, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val posicion = arguments?.getInt("posicion") ?: -1
        var usuarioIdExistente = 0

        if (posicion != -1) {
            // Modo Visualización/Edición
            val usuario = miViewModel.todosLosUsuarios.value?.getOrNull(posicion)
            usuario?.let {
                usuarioIdExistente = it.id
                binding.etUsuarioNombre.setText(it.nombre)
                binding.etUsuarioPassword.setText(it.contrasenia)
            }
        }

        binding.btnCancelarUsuario.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardarUsuario.setOnClickListener {
            val nombre = binding.etUsuarioNombre.text.toString().trim()
            val password = binding.etUsuarioPassword.text.toString().trim()

            if (nombre.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = Usuario(
                id = if (posicion != -1) usuarioIdExistente else 0,
                nombre = nombre,
                contrasenia = password
            )

            if (posicion != -1) {
                miViewModel.actualizarUsuario(usuario)
            } else {
                miViewModel.insertarUsuario(usuario)
            }

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}