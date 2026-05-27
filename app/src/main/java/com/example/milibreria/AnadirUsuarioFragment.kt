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

    private var posicion: Int = -1
    private var usuarioIdExistente: Int = 0

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
                    R.id.action_guardar -> {
                        guardarUsuario()
                        true
                    }

                    R.id.action_cancelar -> {
                        cancelar()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        posicion = arguments?.getInt("posicion") ?: -1

        if (posicion != -1) {
            // Modo Visualización/Edición
            val usuario = miViewModel.todosLosUsuarios.value?.getOrNull(posicion)
            usuario?.let {
                usuarioIdExistente = it.id
                binding.etUsuarioNombre.setText(it.nombre)
                binding.etUsuarioApellido1.setText(it.apellido1)
                binding.etUsuarioApellido2.setText(it.apellido2)
                binding.etUsuarioTelefono.setText(it.telefono)
                binding.swUsuarioAdmin.isChecked = it.admin
                binding.etUsuarioNick.setText(it.usuario)
                binding.etUsuarioPassword.setText(it.contrasenia)
            }
        }

        binding.btnCancelarUsuario.setOnClickListener {
            cancelar()
        }

        binding.btnGuardarUsuario.setOnClickListener {
            guardarUsuario()
        }
    }

    private fun guardarUsuario() {
        val nombre = binding.etUsuarioNombre.text.toString().trim()
        val apellido1 = binding.etUsuarioApellido1.text.toString().trim()
        val apellido2 = binding.etUsuarioApellido2.text.toString().trim()
        val telefono = binding.etUsuarioTelefono.text.toString().trim()
        val isAdmin = binding.swUsuarioAdmin.isChecked
        val nick = binding.etUsuarioNick.text.toString().trim()
        val password = binding.etUsuarioPassword.text.toString().trim()

        if (nombre.isEmpty() || nick.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Nombre, Usuario y Contraseña son obligatorios",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val usuario = Usuario(
            id = if (posicion != -1) usuarioIdExistente else 0,
            nombre = nombre,
            apellido1 = apellido1,
            apellido2 = apellido2,
            telefono = telefono,
            admin = isAdmin,
            usuario = nick,
            contrasenia = password
        )

        if (posicion != -1) {
            miViewModel.actualizarUsuario(usuario)
        } else {
            miViewModel.insertarUsuario(usuario)
        }

        findNavController().navigateUp()
    }

    private fun cancelar() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}