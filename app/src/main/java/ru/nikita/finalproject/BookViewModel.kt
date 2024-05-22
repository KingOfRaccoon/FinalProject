package ru.nikita.finalproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nikita.finalproject.database.Book
import ru.nikita.finalproject.database.LibraryDao
import ru.nikita.finalproject.database.Person
import ru.nikita.finalproject.database.Rent
import java.util.Calendar
import java.util.Date

class BookViewModel(private val libraryDao: LibraryDao) : ViewModel() {
    val personsFlow = libraryDao.getAllPersons()

    val booksFlow =
        combine(
            libraryDao.getAllBooks(),
            libraryDao.getAllRents(),
            personsFlow
        ) { books, rents, people ->
            println("booksFlow")
            println(books)
            println(rents)
            books.map { book ->
                book.copy(rent = rents.find { it.id.toInt() == book.rentId }
                    ?.let { it.copy(user = people.find { person -> person.id == it.userId }) })
            }
        }

    val selectedBook = MutableStateFlow<Book?>(null)
    val nameNewBook = MutableStateFlow("")
    val descNewBook = MutableStateFlow("")
    val nameNewPerson = MutableStateFlow("")

    fun setNewName(newName: String) = nameNewBook.update { newName }
    fun setNewDesc(newDesc: String) = descNewBook.update { newDesc }
    fun setSelectedBook(newBook: Book?) = selectedBook.update { newBook }
    fun setNameNewPerson(newName: String) = nameNewPerson.update { newName }

    fun clearAll() {
        nameNewBook.update { "" }
        descNewBook.update { "" }
    }

    fun setEditBook(book: Book) {
        nameNewBook.update { book.name }
        descNewBook.update { book.description }
    }

    fun returnBook(book: Book) {
        viewModelScope.launch {
            libraryDao.removeRentFromBook(book)
        }
    }

    fun sendBook(book: Book, user: Person) {
        val dt = Date()
        val c = Calendar.getInstance()
        c.setTime(dt)
        c.add(Calendar.DATE, 7)
        val rent = Rent(user.id, dt, c.time)
        viewModelScope.launch {
            libraryDao.addRentToBook(book, rent)
        }
    }

    fun addBook() {
        viewModelScope.launch(Dispatchers.IO) {
            libraryDao.addBook(Book(nameNewBook.value, descNewBook.value))
        }
    }

    fun addPerson() {
        viewModelScope.launch(Dispatchers.IO) {
            libraryDao.addPerson(Person(nameNewPerson.value))
        }
    }
}