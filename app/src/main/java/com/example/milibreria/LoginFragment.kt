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
import com.example.milibreria.databinding.FragmentLoginBinding

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

        binding.btnLoginEntrar.setOnClickListener {
            val usuario = binding.etLoginUsuario.text.toString()
            val contrasenia = binding.etLoginPassword.text.toString()

            var errores = ""
            if (usuario.isEmpty() && contrasenia.isEmpty())
                errores = "Introduce el usuario y la contraseña.\n"
            else if (usuario.isEmpty())
                errores = "Introduce el usuario.\n"
            else if (contrasenia.isEmpty())
                errores = "Introduce la contraseña.\n"

            if (errores.isNotEmpty()) {
                Toast.makeText(
                    requireContext(),
                    errores,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Intentar acceso usando el ViewModel de la Activity
                (activity as MainActivity).miViewModel.autentificar(usuario, contrasenia)

                // Esperar el resultado de la autenticación
                (activity as MainActivity).miViewModel.usuarioActual.observe(viewLifecycleOwner) { usuarioActual ->
                    if (usuarioActual != null) {
                        // Navegación hacia FirstFragment respetando el nav_graph
                        (activity as MainActivity).miViewModel.cargarLibros(usuarioActual.id)
                        (activity as MainActivity).miViewModel.libros.observe(viewLifecycleOwner) {
                            findNavController().navigate(R.id.action_loginFragment_to_menuLibroFragment)
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Credenciales incorrectas",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}