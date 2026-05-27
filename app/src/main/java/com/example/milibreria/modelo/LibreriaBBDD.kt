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
    entities = [Usuario::class, Libro::class, Prestamo::class],
    version = 3,
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
                    // Insertar el usuario inicial con todos sus campos y admin = true
                    val usuarioAdmin = Usuario(
                        id = 0,
                        nombre = "Admin",
                        apellido1 = "Biblioteca",
                        apellido2 = "Principal",
                        telefono = "600000000",
                        admin = true, // Es admin; puede loguearse
                        usuario = "abcd",
                        contrasenia = "1234"
                    )
                    INSTANCE?.libreriaDAO?.insertarUsuario(usuarioAdmin)

                    // Autenticar usando el campo 'usuario'
                    val usuarioInsertado = INSTANCE
                        ?.libreriaDAO
                        ?.autenticar(usuarioAdmin.usuario, usuarioAdmin.contrasenia)
                        ?.first()

                    val userId = usuarioInsertado?.id ?: 0

                    val librosIniciales = listOf(
                        Libro(
                            titulo = "Don Quijote de la Mancha",
                            autor = "Miguel de Cervantes",
                            isbn = "9788424116286",
                            publicacion = 1605,
                            usuario_id = userId
                        ),
                        Libro(
                            titulo = "Cien años de soledad",
                            autor = "Gabriel García Márquez",
                            isbn = "9780307474728",
                            publicacion = 1967,
                            usuario_id = userId
                        ),
                        Libro(
                            titulo = "El Principito",
                            autor = "Antoine de Saint-Exupéry",
                            isbn = "9780156013925",
                            publicacion = 1943,
                            usuario_id = userId
                        ),
                    )

                    librosIniciales.forEach { libro -> INSTANCE?.libreriaDAO?.insertarLibro(libro) }

                    // --- AÑADIR PRÉSTAMOS INICIALES DE PRUEBA (Para instalaciones desde cero) ---
                    // Recuperar el ID del primer libro que se acaba de insertar para no romper la clave foránea
                    val librosInsertados = INSTANCE?.libreriaDAO?.cargarLibros()?.first()
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