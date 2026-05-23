package com.example.milibreria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.milibreria.adaptador.AdaptadorUsuarios
import com.example.milibreria.databinding.FragmentColeccionUsuariosBinding
import com.example.milibreria.modelo.VM

class ColeccionUsuariosFragment : Fragment() {

    private var _binding: FragmentColeccionUsuariosBinding? = null
    private val binding get() = _binding!!
    private val miViewModel: VM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColeccionUsuariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_coleccion_usuarios, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.volver -> {
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.rvColeccionUsuarios.layoutManager = LinearLayoutManager(requireContext())

        miViewModel.cargarUsuarios()
        miViewModel.todosLosUsuarios.observe(viewLifecycleOwner) { listaUsuarios ->
            if (listaUsuarios != null) {
                binding.rvColeccionUsuarios.adapter = AdaptadorUsuarios(listaUsuarios) { posicion ->
                    val bundle = bundleOf("posicion" to posicion)
                    findNavController().navigate(
                        R.id.action_coleccionUsuariosFragment_to_anadirUsuarioFragment,
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