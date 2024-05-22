package ru.nikita.finalproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddBookScreen(
    name: String,
    desc: String,
    updateName: (String) -> Unit,
    updateDesc: (String) -> Unit,
    addBookAction: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp), Arrangement.spacedBy(16.dp)) {
        TextField(name, updateName, Modifier.fillMaxWidth(), label = { Text("Название книги") })
        TextField(desc, updateDesc, Modifier.fillMaxWidth(), label = { Text("Описание книги") })
        Spacer(Modifier.weight(1f))
        Button(addBookAction, Modifier.fillMaxWidth()){
            Text("Добавить книгу")
        }
    }
}