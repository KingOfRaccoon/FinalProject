package ru.nikita.finalproject

import android.app.Application
import androidx.room.Room
import ru.nikita.finalproject.database.LibraryDatabase

class LibraryApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            LibraryDatabase::class.java,
            "database"
        ).build()
    }
}