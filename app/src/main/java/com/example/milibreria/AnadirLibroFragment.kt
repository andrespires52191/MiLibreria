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
import com.example.milibreria.databinding.FragmentAnadirLibroBinding
import com.example.milibreria.modelo.Libro
import com.example.milibreria.modelo.VM

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

        // Configuración del menú superior (Guardar / Cancelar desde la Toolbar)
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
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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
                usuario_id = miViewModel.usuarioActual.value!!.id
            )
        }

        // Rellenar los campos con los datos del libro (vacíos si es un libro nuevo)
        binding.etDetalleTitulo.setText(libro.titulo)
        binding.etDetalleAutor.setText(libro.autor)
        binding.etDetalleIsbn.setText(libro.isbn)
        binding.etDetallePublicacion.setText(libro.publicacion?.toString() ?: "")
        binding.etDetalleValoracion.setText(libro.valoracion?.toString() ?: "")

        // Botón Cancelar
        binding.btnCancelar.setOnClickListener {
            cancelar()
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            guardarLibro()
        }
    }

    private fun guardarLibro() {
        val tituloText = binding.etDetalleTitulo.text.toString().trim()

        // Validación básica obligatoria: El título no puede estar vacío
        if (tituloText.isEmpty()) {
            Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        libro.titulo = tituloText
        libro.autor = binding.etDetalleAutor.text.toString().trim().takeIf { it.isNotEmpty() }
        libro.isbn = binding.etDetalleIsbn.text.toString().trim().takeIf { it.isNotEmpty() }

        // Conversión directa a Int del año tecleado por el usuario
        libro.publicacion = binding.etDetallePublicacion.text.toString().toIntOrNull()
        libro.valoracion = binding.etDetalleValoracion.text.toString().toDoubleOrNull()

        if (posicion != -1) {
            // Modo Edición: Modifica el libro existente en la base de datos
            miViewModel.actualizarLibro(libro)
        } else {
            // Modo Añadir: Agrega el nuevo libro a la base de datos
            miViewModel.insertarLibro(libro)
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