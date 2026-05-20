package com.example.milibreria.modelo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VM(val miRepo: Repositorio) : ViewModel() {
    lateinit var usuarioActual: LiveData<Usuario?>
    lateinit var libros: LiveData<List<Libro>>

    fun autentificar(usuario: String, contrasenia: String) {
        usuarioActual = miRepo.autenticar(usuario, contrasenia).asLiveData()
    }

    fun cargarLibros(usuarioID: Int) {
        libros = miRepo.cargarLibros(usuarioID).asLiveData()
    }

    fun getLibro(posicion: Int): Libro? {
        return libros.value?.get(posicion)
    }

    fun actualizarLibro(libro: Libro) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.actualizarLibro(libro)
    }

    fun insertarLibro(libro: Libro) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.insertarLibro(libro)
    }
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
