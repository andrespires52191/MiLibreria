package com.example.milibreria

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentAnadirLibroBinding
import com.example.milibreria.modelo.Libro
import com.example.milibreria.modelo.VM
import java.util.Calendar

class AnadirLibroFragment : Fragment() {

    private var _binding: FragmentAnadirLibroBinding? = null
    private val binding get() = _binding!!

    // Acceder al mismo ViewModel que comparte la MainActivity
    private val miViewModel: VM by activityViewModels()

    private var posicion: Int = -1
    private lateinit var libro: Libro

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnadirLibroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_anadir_libro, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_guardar -> {
                        guardarLibro()
                        true
                    }
                    R.id.action_cancelar -> {
                        cancelar()
                        true
                    }
                    else -> false
                }
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Si no se recibe posición, el valor por defecto será "-1" (Modo Añadir)
        posicion = arguments?.getInt("posicion") ?: -1

        libro = if (posicion != -1) {
            miViewModel.getLibro(posicion)!!
        } else {
            Libro(
                titulo = "",
                autor = null,
                isbn = null,
                publicacion = null,
                valoracion = null,
                usuario_id = (activity as MainActivity).miViewModel.usuarioActual.value?.id ?: 0
            )
        }

        binding.etDetalleTitulo.setText(libro.titulo)
        binding.etDetalleAutor.setText(libro.autor)
        binding.etDetalleIsbn.setText(libro.isbn)
        binding.etDetallePublicacion.setText(libro.publicacion?.toString() ?: "")
        binding.etDetalleValoracion.setText(libro.valoracion?.toString() ?: "")

        // Configuración del botón para abrir el Calendario (DatePickerDialog)
        binding.btnCalendario.setOnClickListener {
            val calendar = Calendar.getInstance()
            val anioActual = calendar.get(Calendar.YEAR)
            val mesActual = calendar.get(Calendar.MONTH)
            val diaActual = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, _, _ ->
                // Guardar únicamente el año seleccionado
                binding.etDetallePublicacion.setText(year.toString())
            }, anioActual, mesActual, diaActual)

            datePicker.show()
        }

        // Botón Cancelar: Vuelve atrás sin guardar cambios
        binding.btnCancelar.setOnClickListener {
            cancelar()
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            guardarLibro()
        }
    }

    private fun guardarLibro() {
        libro.titulo = binding.etDetalleTitulo.text.toString()
        libro.autor = binding.etDetalleAutor.text.toString()
        libro.isbn = binding.etDetalleIsbn.text.toString()
        libro.publicacion = binding.etDetallePublicacion.text.toString().toIntOrNull()
        libro.valoracion = binding.etDetalleValoracion.text.toString().toDoubleOrNull()

        if (posicion != -1) {
            // Modo Edición: Modifica el libro existente en la lista
            (activity as MainActivity).miViewModel.actualizarLibro(libro)
        } else {
            // Modo Añadir: Agrega el nuevo libro al ViewModel
            (activity as MainActivity).miViewModel.insertarLibro(libro)
        }

        // Regresa a la pantalla anterior del flujo de navegación
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