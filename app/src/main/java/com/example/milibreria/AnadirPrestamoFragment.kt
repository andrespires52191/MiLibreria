package com.example.milibreria

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.milibreria.databinding.FragmentAnadirPrestamoBinding
import com.example.milibreria.modelo.Libro
import com.example.milibreria.modelo.Prestamo
import com.example.milibreria.modelo.Usuario
import com.example.milibreria.modelo.VM
import java.util.Calendar

class AnadirPrestamoFragment : Fragment() {

    private var _binding: FragmentAnadirPrestamoBinding? = null
    private val binding get() = _binding!!

    // Compartimos el mismo ViewModel de la MainActivity con activityViewModels()
    private val miViewModel: VM by activityViewModels()

    // Listas auxiliares para asociar las posiciones del Spinner con las IDs reales de Room
    private var listaLibrosDisponibles: List<Libro> = listOf()
    private var listaUsuariosDisponibles: List<Usuario> = listOf()

    private var posicion: Int = -1
    private var prestamoIdExistente: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnadirPrestamoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_anadir_prestamo, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_guardar -> {
                        guardarPrestamo()
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

        // Recuperar argumentos (si viene -1 es un préstamo nuevo, si no, es visualización/edición)
        posicion = arguments?.getInt("posicion") ?: -1

        // 1. Cargar datos obligatorios en los Spinners (Desplegables)
        // Para prestar necesitamos saber qué libros y qué usuarios existen en el sistema global
        miViewModel.cargarUsuarios()

        // Observamos los usuarios globales para llenar su Spinner
        miViewModel.todosLosUsuarios.observe(viewLifecycleOwner) { usuarios ->
            if (usuarios != null) {
                listaUsuariosDisponibles = usuarios
                val nombresUsuarios = usuarios.map { it.nombre }
                val adapterUsuarios = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresUsuarios)
                adapterUsuarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerUsuarios.adapter = adapterUsuarios

                // Si estamos visualizando, pre-seleccionamos el usuario correcto
                if (posicion != -1) {
                    val prestamoDetalle = miViewModel.getPrestamoDetallado(posicion)
                    val index = usuarios.indexOfFirst { it.id == prestamoDetalle?.usuario?.id }
                    if (index != -1) binding.spinnerUsuarios.setSelection(index)
                }
            }
        }

        // Observamos los libros (asociados al usuario logueado en vuestro flujo habitual) para llenar su Spinner
        miViewModel.libros.observe(viewLifecycleOwner) { libros ->
            if (libros != null) {
                listaLibrosDisponibles = libros
                val titulosLibros = libros.map { it.titulo }
                val adapterLibros = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, titulosLibros)
                adapterLibros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerLibros.adapter = adapterLibros

                // Si estamos visualizando, pre-seleccionamos el libro correcto
                if (posicion != -1) {
                    val prestamoDetalle = miViewModel.getPrestamoDetallado(posicion)
                    val index = libros.indexOfFirst { it.id == prestamoDetalle?.libro?.id }
                    if (index != -1) binding.spinnerLibros.setSelection(index)
                }
            }
        }

        // 2. Configurar la vista si es Modo Visualización/Edición
        if (posicion != -1) {
            val prestamoDetalle = miViewModel.getPrestamoDetallado(posicion)
            prestamoDetalle?.let {
                prestamoIdExistente = it.prestamo.id
                binding.etFechaInicio.setText(it.prestamo.fechaInicio)
                binding.etFechaFin.setText(it.prestamo.fechaFin)

                // Si solo queremos ver los detalles sin que editen por error, podemos desactivar los controles:
                // binding.spinnerLibros.isEnabled = false
                // binding.spinnerUsuarios.isEnabled = false
            }
        }

        // 3. Configurar los DatePickers de las fechas (Igual que hicisteis con publicación)
        binding.etFechaInicio.setOnClickListener { mostrarDatePicker { fecha -> binding.etFechaInicio.setText(fecha) } }
        binding.etFechaFin.setOnClickListener { mostrarDatePicker { fecha -> binding.etFechaFin.setText(fecha) } }

        // 4. Botón Cancelar
        binding.btnCancelarPrestamo.setOnClickListener {
            cancelar()
        }

        // 5. Botón Guardar
        binding.btnGuardarPrestamo.setOnClickListener {
            guardarPrestamo()
        }
    }

    private fun guardarPrestamo() {
        val fechaInicio = binding.etFechaInicio.text.toString()
        val fechaFin = binding.etFechaFin.text.toString()

        // Validar que se haya seleccionado algo y que las fechas no estén vacías
        if (listaLibrosDisponibles.isEmpty() || listaUsuariosDisponibles.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        // Obtenemos los IDs reales correspondientes a los elementos seleccionados en los Spinners
        val libroSeleccionado = listaLibrosDisponibles[binding.spinnerLibros.selectedItemPosition]
        val usuarioSeleccionado = listaUsuariosDisponibles[binding.spinnerUsuarios.selectedItemPosition]

        // Instanciamos el objeto Prestamo
        val prestamo = Prestamo(
            id = if (posicion != -1) prestamoIdExistente else 0,
            libro_id = libroSeleccionado.id,
            usuario_id = usuarioSeleccionado.id,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin
        )

        // Guardar o actualizar a través del ViewModel
        if (posicion != -1) {
            miViewModel.actualizarPrestamo(prestamo)
        } else {
            miViewModel.insertarPrestamo(prestamo)
        }

        // Volver atrás en el flujo
        findNavController().navigateUp()
    }

    private fun cancelar() {
        findNavController().navigateUp()
    }

    // Función auxiliar para desplegar el selector de fechas de Android de manera limpia
    private fun mostrarDatePicker(onFechaSeleccionada: (String) -> Unit) {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val fechaFormateada = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
            onFechaSeleccionada(fechaFormateada)
        }, anio, mes, dia).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}