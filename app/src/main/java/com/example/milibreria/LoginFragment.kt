package com.example.milibreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentLoginBinding
import com.example.milibreria.modelo.VM

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val miViewModel: VM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginEntrar.setOnClickListener {
            val usuario = binding.etLoginUsuario.text.toString()
            val contrasenia = binding.etLoginPassword.text.toString()

            if (usuario.isNotEmpty() && contrasenia.isNotEmpty()) {
                miViewModel.autentificar(usuario, contrasenia)

                // Redirige al HomeFragment
                findNavController().navigate(R.id.action_loginFragment_to_menuLibroFragment)
            } else {
                Toast.makeText(requireContext(), "Por favor, introduce tus credenciales", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}