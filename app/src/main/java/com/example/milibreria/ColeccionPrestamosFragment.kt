package com.example.milibreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.milibreria.adaptador.AdaptadorPrestamos
import com.example.milibreria.databinding.FragmentColeccionPrestamosBinding
import com.example.milibreria.modelo.VM

class ColeccionPrestamosFragment : Fragment() {

    private var _binding: FragmentColeccionPrestamosBinding? = null
    private val binding get() = _binding!!
    private val miViewModel: VM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColeccionPrestamosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvColeccionPrestamos.layoutManager = LinearLayoutManager(requireContext())

        miViewModel.cargarPrestamos()
        miViewModel.todosLosPrestamos.observe(viewLifecycleOwner) { listaPrestamos ->
            if (listaPrestamos != null) {
                binding.rvColeccionPrestamos.adapter = AdaptadorPrestamos(listaPrestamos) { posicion ->
                    val bundle = bundleOf("posicion" to posicion)
                    findNavController().navigate(
                        R.id.action_coleccionPrestamosFragment_to_anadirPrestamoFragment,
                        bundle
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}