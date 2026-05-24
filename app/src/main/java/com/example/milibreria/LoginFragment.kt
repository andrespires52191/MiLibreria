package com.example.milibreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentLoginBinding
import com.example.milibreria.modelo.Usuario

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observarAutentificacion()
        observarRegistro()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_login, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Botón habitual de Iniciar Sesión
        binding.btnLoginEntrar.setOnClickListener {
            val usuario = binding.etLoginUsuario.text.toString().trim()
            val contrasenia = binding.etLoginPassword.text.toString().trim()

            if (usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena todos los campos.",
                    Toast.LENGTH_SHORT).show()
            } else {
                (activity as MainActivity).miViewModel.autentificar(usuario, contrasenia)
            }
        }

        // Botón para Registrarse y también Iniciar Sesión
        binding.btnRegistrarAhora.setOnClickListener {
            val usuario = binding.etLoginUsuario.text.toString().trim()
            val contrasenia = binding.etLoginPassword.text.toString().trim()

            if (usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Introduce usuario y contraseña para registrarte.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val usuarioNuevo = Usuario(nombre = usuario, contrasenia = contrasenia)
                (activity as MainActivity).miViewModel.registrarYAutentificar(usuarioNuevo)
            }
        }
    }

    private fun observarAutentificacion() {
        (activity as MainActivity).miViewModel.usuarioActual.observe(viewLifecycleOwner) {
            usuarioActual ->
            if (usuarioActual != null) {
                // Navegar inmediatamente al menú principal
                findNavController().navigate(R.id.action_loginFragment_to_menuLibroFragment)
            } else {
                // Solo mostrar error si el usuario ha intentado escribir algo
                if (binding.etLoginUsuario.text.isNotEmpty() &&
                    binding.etLoginPassword.text.isNotEmpty()) {
                    Toast.makeText(requireContext(), "Credenciales incorrectas",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun observarRegistro() {
        (activity as MainActivity).miViewModel.mensajeRegistro.observe(viewLifecycleOwner) {
            mensaje ->
            if (mensaje != null) {
                Toast.makeText(requireContext(), mensaje,
                    Toast.LENGTH_LONG).show()
                // Limpiar el LiveData una vez mostrado el Toast de aviso para que no repita
                (activity as MainActivity).miViewModel.mensajeRegistro.value = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}