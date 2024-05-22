package ru.nikita.finalproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.nikita.finalproject.database.Book
import ru.nikita.finalproject.database.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    books: List<Book>,
    persons: List<Person>,
    selectedBook: Book?,
    namePerson: String,
    updateNamePerson: (String) -> Unit,
    addPerson: () -> Unit,
    setBook: (Book?) -> Unit,
    returnAction: (Book) -> Unit,
    sendAction: (Book, Person) -> Unit,
    navigateToEdit: (Book) -> Unit
) {
    val stateBottomSheet = rememberModalBottomSheetState()
    val personsStateBottomSheet = rememberModalBottomSheetState()
    var setPerson by remember { mutableStateOf(false) }
    var addPersonDialog by remember { mutableStateOf(false) }

    if (addPersonDialog)
        AddPersonDialog({ addPersonDialog = false }, namePerson, updateNamePerson, addPerson)

    if (setPerson)
        PersonsBottomSheet(
            personsStateBottomSheet,
            { setPerson = false },
            persons,
            {
                selectedBook?.let { book ->
                    sendAction(book, it); setPerson = false; setBook(null)
                }
            },
            { addPersonDialog = true }
        )

    selectedBook?.let {
        DetailBottomSheet(
            stateBottomSheet,
            { setBook(null) },
            selectedBook,
            { navigateToEdit(selectedBook); setBook(null) },
            { returnAction(selectedBook); setBook(null) },
            { setPerson = true })
    }

    LazyColumn(Modifier.fillMaxSize()) {
        items(books) {
            ItemBook(it, setBook)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ItemBook(book: Book, openDialog: (Book) -> Unit) {
    Card({ openDialog(book) }, Modifier.fillMaxWidth().padding(horizontal = 7.dp)) {
        Column(Modifier.fillMaxWidth().padding(11.dp, 9.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(book.name, Modifier.weight(1f))
                if (book.rent != null)
                    AssistChip({}, { Text("Книга занята") })
            }

            book.rent?.let {
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip({}, { Text("Книга занята до: " + it.dateEnd.toLocaleString()) })
                    it.user?.let {
                        AssistChip({}, { Text("У пользователя: ${it.username}") })
                    }
                }
            }
        }
    }
}

@Composable
private fun AddPersonDialog(
    onDismiss: () -> Unit,
    namePerson: String,
    updateNamePerson: (String) -> Unit,
    addPerson: () -> Unit
) {
    Dialog(onDismiss) {
        Card(Modifier.fillMaxWidth().padding(16.dp)) {
            Column(
                Modifier.fillMaxWidth().padding(16.dp),
                Arrangement.spacedBy(16.dp),
                Alignment.CenterHorizontally
            ) {
                TextField(
                    namePerson,
                    updateNamePerson,
                    Modifier.fillMaxWidth(),
                    label = { Text("Имя") })

                Button({ addPerson(); onDismiss() }, Modifier.fillMaxWidth()) {
                    Text("Добавить пользователя")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonsBottomSheet(
    state: SheetState,
    dismissBottomSheet: () -> Unit,
    persons: List<Person>,
    selectPerson: (Person) -> Unit,
    clickToAddPerson: () -> Unit
) {
    ModalBottomSheet(dismissBottomSheet, Modifier.fillMaxWidth(), state) {
        LazyColumn(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(persons) {
                ItemPerson(it) { selectPerson(it) }
            }

            item {
                Spacer(Modifier.weight(1f))
                Button(clickToAddPerson, Modifier.fillMaxWidth()) {
                    Text("Добавить нового пользователя")
                }
            }
        }
    }
}

@Composable
fun ItemPerson(person: Person, selectPerson: () -> Unit) {
    Card(selectPerson, Modifier.fillMaxWidth()) {
        Text(person.username, Modifier.fillMaxWidth().padding(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailBottomSheet(
    state: SheetState,
    dismissBottomSheet: () -> Unit,
    book: Book,
    editAction: () -> Unit,
    returnAction: () -> Unit,
    sendAction: () -> Unit
) {
    ModalBottomSheet(dismissBottomSheet, Modifier.fillMaxWidth(), state) {
        Column(Modifier.fillMaxWidth().padding(16.dp), Arrangement.spacedBy(16.dp)) {
            Text(book.name)
            Text(book.description)
            book.rent?.let {
                Text("Книга занята до: " + it.dateEnd.toLocaleString(), color = Color.Red)
            }
            book.rent?.user?.let {
                Text("У пользователя: ${it.username}")
            }

            Button(editAction, Modifier.fillMaxWidth()) {
                Text("Редактировать")
            }

            Button(if (book.rentId != null) returnAction else sendAction, Modifier.fillMaxWidth()) {
                Text(if (book.rentId != null) "Вернуть" else "Отдать")
            }
        }
    }
}