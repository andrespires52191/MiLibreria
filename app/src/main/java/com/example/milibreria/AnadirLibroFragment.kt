package com.example.milibreria

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnadirLibroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar índice del libro seleccionado
        // Si no se recibe posición, el valor por defecto será "-1" (Modo Añadir)
        val posicion = arguments?.getInt("posicion") ?: -1

        if (posicion != -1) {
            // MODO EDICIÓN: Carga los datos del libro existente
            val libro = miViewModel.listaLibros()[posicion]
            binding.etDetalleTitulo.setText(libro.titulo)
            binding.etDetalleAutor.setText(libro.autor)
            binding.etDetalleIsbn.setText(libro.isbn)
            binding.etDetallePublicacion.setText(libro.publicacion.toString())
            binding.etDetalleValoracion.setText(libro.valoracion.toString())
        } else {
            // MODO AÑADIR: Asegura que todos los campos comiencen completamente vacíos
            binding.etDetalleTitulo.setText("")
            binding.etDetalleAutor.setText("")
            binding.etDetalleIsbn.setText("")
            binding.etDetallePublicacion.setText("")
            binding.etDetalleValoracion.setText("")
        }

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
            findNavController().navigateUp()
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            val titulo = binding.etDetalleTitulo.text.toString()
            val autor = binding.etDetalleAutor.text.toString()
            val isbn = binding.etDetalleIsbn.text.toString()
            val publicacion = binding.etDetallePublicacion.text.toString().toIntOrNull() ?: 0
            val valoracion = binding.etDetalleValoracion.text.toString().toDoubleOrNull() ?: 0.0

            if (posicion != -1) {
                // Modo Edición: Modifica el libro existente en la lista
                val libroExistente = miViewModel.listaLibros()[posicion]
                libroExistente.titulo = titulo
                libroExistente.autor = autor
                libroExistente.isbn = isbn
                libroExistente.publicacion = publicacion
                libroExistente.valoracion = valoracion
            } else {
                // Modo Añadir: Genera un ID nuevo y agrega el nuevo libro al ViewModel
                val nuevaLista = miViewModel.listaLibros()
                val nuevoId = if (nuevaLista.isNotEmpty()) nuevaLista.last().id + 1 else 0
                val nuevoLibro = Libro(nuevoId, titulo, autor, isbn, publicacion, valoracion)
                nuevaLista.add(nuevoLibro)
            }

            // Regresa a la pantalla anterior del flujo de navegación
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}