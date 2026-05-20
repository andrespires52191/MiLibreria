package com.example.milibreria.modelo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first


@Database(
    entities = [
        Usuario::class,
        Libro::class
    ],
    version = 1
)
abstract class LibreriaBBDD : RoomDatabase() {

    abstract val libreriaDAO: LibreriaDAO

    companion object {
        @Volatile
        private var INSTANCE: LibreriaBBDD? = null


        // Callback que se ejecuta cuando se crea la base de datos
        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val viewModelScope = CoroutineScope(Dispatchers.IO)
                viewModelScope.launch {
                    // insertar usuario y obtener su id a través de la consulta de autenticación
                    val usuario = Usuario(0, "abcd", "1234")

                    INSTANCE?.libreriaDAO?.insertarUsuario(usuario)

                    // autenticar devuelve un Flow<Usuario?>, obtener el primer valor
                    val usuarioInsertado = INSTANCE
                        ?.libreriaDAO
                        ?.autenticar(usuario.nombre, usuario.contrasenia)
                        ?.first()

                    val userId = usuarioInsertado?.id ?: 0

                    val librosIniciales = listOf(
                        Libro(
                            titulo = "titulo_1",
                            autor = "autor_1",
                            isbn = "isbn_1",
                            publicacion = 1999,
                            valoracion = 4.0,
                            usuario_id = userId
                        ),
                        Libro(
                            titulo = "titulo_2",
                            autor = "autor_2",
                            isbn = "isbn_2",
                            publicacion = 1999,
                            valoracion = 4.0,
                            usuario_id = userId
                        ),
                        Libro(
                            titulo = "titulo_3",
                            autor = "autor_3",
                            isbn = "isbn_3",
                            publicacion = 1999,
                            valoracion = 4.0,
                            usuario_id = userId
                        ),
                    )

                    librosIniciales.forEach { libro -> INSTANCE?.libreriaDAO?.insertarLibro(libro) }
                }
            }
        }

        fun getInstance(context: Context): LibreriaBBDD {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LibreriaBBDD::class.java,
                    "libreria_db"
                )
                    .addCallback(roomCallback)
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}
