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
        val posicion = arguments?.getInt("posicion") ?: 0
        var libro = miViewModel.getLibro(posicion)

        // En vez de hacer esto aquí se puede hacer la diferenciación entre
        // El caso de editar y el caso de añadir
        libro = libro!!

        // Mostrar datos iniciales en los EditText correspondientes
        binding.etDetalleTitulo.setText(libro.titulo)
        binding.etDetalleAutor.setText(libro.autor)
        binding.etDetalleIsbn.setText(libro.isbn)
        binding.etDetallePublicacion.setText(libro.publicacion.toString())
        binding.etDetalleValoracion.setText(libro.valoracion.toString())

        // Configuración del botón para abrir el Calendario (DatePickerDialog)
        binding.btnCalendario.setOnClickListener {
            val calendar = Calendar.getInstance()
            val anioActual = calendar.get(Calendar.YEAR)
            val mesActual = calendar.get(Calendar.MONTH)
            val diaActual = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, _, _ ->
                // Guardar únicamente el año seleccionado como se ve en tu modelo de datos
                binding.etDetallePublicacion.setText(year.toString())
            }, anioActual, mesActual, diaActual)

            datePicker.show()
        }

        // Botón Cancelar: Vuelve atrás sin guardar cambios
        binding.btnCancelar.setOnClickListener {
            findNavController().navigateUp()
        }

        // Botón Guardar: Actualiza el objeto en la lista y regresa
        binding.btnGuardar.setOnClickListener {
            libro.titulo = binding.etDetalleTitulo.text.toString()
            libro.autor = binding.etDetalleAutor.text.toString()
            libro.isbn = binding.etDetalleIsbn.text.toString()
            libro.publicacion = binding.etDetallePublicacion.text.toString().toIntOrNull() ?: 0
            libro.valoracion = binding.etDetalleValoracion.text.toString().toDoubleOrNull() ?: 0.0

            (activity as MainActivity).miViewModel.actualizarLibro(libro)

            // Regresa a la ventana de la Colección mostrando los cambios actualizados
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}