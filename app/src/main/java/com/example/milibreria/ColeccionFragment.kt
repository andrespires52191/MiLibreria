package com.example.milibreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentColeccionBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ColeccionFragment : Fragment() {

    private var _binding: FragmentColeccionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentColeccionBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val libros = (activity as MainActivity).miViewModel.listaLibros()
        var lista = ""
        for (libro in libros) {
            lista = lista + libro.titulo + " - " + libro.autor + "\n"
        }
        binding.tvListaTexto.text = lista

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_ColeccionFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
