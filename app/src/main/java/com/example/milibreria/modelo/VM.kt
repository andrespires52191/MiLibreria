package com.example.milibreria.modelo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.milibreria.modelo.relaciones.PrestamoDetallado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VM(val miRepo: Repositorio) : ViewModel() {
    lateinit var libros: LiveData<List<Libro>>
    lateinit var usuarioActual: LiveData<Usuario?>
    lateinit var todosLosUsuarios: LiveData<List<Usuario>>
    lateinit var todosLosPrestamos: LiveData<List<PrestamoDetallado>>

    // === LIBROS ===

    fun insertarLibro(libro: Libro) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.insertarLibro(libro)
    }

    fun actualizarLibro(libro: Libro) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.actualizarLibro(libro)
    }

    fun cargarLibros(usuarioID: Int) {
        libros = miRepo.cargarLibros(usuarioID).asLiveData()
    }

    fun getLibro(posicion: Int): Libro? {
        return libros.value?.get(posicion)
    }

    // === USUARIOS ===

    fun insertarUsuario(usuario: Usuario) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.insertarUsuario(usuario)
    }

    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.actualizarUsuario(usuario)
    }

    fun cargarUsuarios() {
        todosLosUsuarios = miRepo.cargarUsuarios().asLiveData()
    }

    fun getUsuario(posicion: Int): Usuario? = todosLosUsuarios.value?.get(posicion)

    fun autentificar(usuario: String, contrasenia: String) {
        usuarioActual = miRepo.autenticar(usuario, contrasenia).asLiveData()
    }

    // === PRÉSTAMOS ===

    fun insertarPrestamo(prestamo: Prestamo) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.insertarPrestamo(prestamo)
    }

    fun actualizarPrestamo(prestamo: Prestamo) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.actualizarPrestamo(prestamo)
    }

    fun cargarPrestamos() {
        todosLosPrestamos = miRepo.cargarPrestamos().asLiveData()
    }

    fun getPrestamoDetallado(posicion: Int): PrestamoDetallado? = todosLosPrestamos.value?.get(posicion)
}

class LibreriaViewModelFactory(private val miRepo: Repositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VM(miRepo) as T
        }
        throw IllegalArgumentException("ViewModel class desconocida")
    }
}
