package com.example.milibreria.modelo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.milibreria.modelo.relaciones.PrestamoDetallado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VM(val miRepo: Repositorio) : ViewModel() {

    private var _usuarioActual = MutableLiveData<Usuario?>()
    val usuarioActual: LiveData<Usuario?> get() = _usuarioActual

    private var _libros: LiveData<List<Libro>> = MutableLiveData()
    val libros: LiveData<List<Libro>> get() = _libros

    lateinit var todosLosUsuarios: LiveData<List<Usuario>>
    lateinit var todosLosPrestamos: LiveData<List<PrestamoDetallado>>

    val mensajeRegistro = MutableLiveData<String?>()

    // === AUTENTICACIÓN Y REGISTRO ===

    fun autentificar(usuario: String, contrasenia: String) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.autenticar(usuario, contrasenia).collect { usuarioLogueado ->
            _usuarioActual.postValue(usuarioLogueado)
        }
    }

    fun registrarYAutentificar(usuario: Usuario) = viewModelScope.launch(Dispatchers.IO) {
        val usuarioExistente = miRepo.buscarUsuarioPorNombre(usuario.nombre)
        if (usuarioExistente != null) {
            mensajeRegistro.postValue("El usuario ya existe")
            return@launch
        }
        mensajeRegistro.postValue(null)

        // 1. Insertamos el usuario en la base de datos
        miRepo.insertarUsuario(usuario)

        // 2. Buscamos el usuario recién creado para obtener su ID generado por Room
        val usuarioRegistrado = miRepo.buscarUsuarioPorNombre(usuario.nombre)

        // 3. Le avisamos al Fragment en el hilo principal con postValue para que navegue de inmediato
        _usuarioActual.postValue(usuarioRegistrado)
    }

    // === LIBROS ===

    fun insertarLibro(libro: Libro) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.insertarLibro(libro)
    }

    fun actualizarLibro(libro: Libro) = viewModelScope.launch(Dispatchers.IO) {
        miRepo.actualizarLibro(libro)
    }

    fun cargarLibros(usuarioID: Int) {
        _libros = miRepo.cargarLibros(usuarioID).asLiveData()
    }

    fun getLibro(posicion: Int): Libro? {
        return libros.value?.getOrNull(posicion)
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

    fun getUsuario(posicion: Int): Usuario? = todosLosUsuarios.value?.getOrNull(posicion)

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

    fun getPrestamoDetallado(posicion: Int): PrestamoDetallado? = todosLosPrestamos.value?.getOrNull(posicion)
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