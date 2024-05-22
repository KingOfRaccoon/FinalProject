package ru.nikita.finalproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {

    @Query("select * from person")
    fun getAllPersons(): Flow<List<Person>>

    @Query("select * from rent")
    fun getAllRents(): Flow<List<Rent>>

    @Query("select * from book")
    fun getAllBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(book: Book)

    @Insert
    suspend fun addPerson(person: Person)

    @Delete
    suspend fun deleteBook(book: Book)

    @Insert
    suspend fun addRent(rent: Rent)

    @Delete
    suspend fun deleteRent(rent: Rent): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBook(book: Book): Int

    @Query("select count() from rent")
    fun getRentId(): Int

    @Transaction
    suspend fun removeRentFromBook(book: Book) {
        println("removeRentFromBook: ${book.rent}")
        book.rentId = null
        book.rent = null
        book.rent?.let { println(deleteRent(it)) }
    }

    @Transaction
    suspend fun addRentToBook(book: Book, rent: Rent) {
        addRent(rent)
        book.rentId = getRentId()
        println(updateBook(book))
    }
}