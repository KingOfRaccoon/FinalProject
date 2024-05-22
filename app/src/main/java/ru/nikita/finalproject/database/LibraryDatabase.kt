package ru.nikita.finalproject.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([Person::class, Rent::class, Book::class], version = 1)
abstract class LibraryDatabase: RoomDatabase() {
    abstract fun getDao(): LibraryDao
}