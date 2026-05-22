package com.example.milibreria.modelo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first


@Database(
    entities = [
        Usuario::class,
        Libro::class,
        Prestamo::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LibreriaBBDD : RoomDatabase() {

    abstract val libreriaDAO: LibreriaDAO

    companion object {
        @Volatile
        private var INSTANCE: LibreriaBBDD? = null

        // --- DEFINICIÓN DE LA MIGRACIÓN ---
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `Prestamo` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `libro_id` INTEGER NOT NULL, 
                        `usuario_id` INTEGER NOT NULL, 
                        `fechaInicio` TEXT NOT NULL, 
                        `fechaFin` TEXT NOT NULL, 
                        FOREIGN KEY(`libro_id`) REFERENCES `Libro`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION, 
                        FOREIGN KEY(`usuario_id`) REFERENCES `Usuario`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION 
                    )
                """.trimIndent()
                )
            }
        }

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

                    // --- AÑADIR PRÉSTAMOS INICIALES DE PRUEBA (Para instalaciones desde cero) ---
                    // Recuperar el ID del primer libro que se acaba de insertar para no romper la clave foránea
                    val librosInsertados = INSTANCE?.libreriaDAO?.cargarLibros(userId)?.first()
                    val libroId = librosInsertados?.firstOrNull()?.id ?: 0

                    if (libroId != 0) {
                        val prestamoInicial = Prestamo(
                            id = 0,
                            libro_id = libroId,
                            usuario_id = userId,
                            fechaInicio = "22/05/2026",
                            fechaFin = "05/06/2026"
                        )
                        INSTANCE?.libreriaDAO?.insertarPrestamo(prestamoInicial)
                    }
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
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(roomCallback)
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}
