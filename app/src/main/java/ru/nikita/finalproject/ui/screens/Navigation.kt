package ru.nikita.finalproject.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.nikita.finalproject.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(viewModel: BookViewModel) {
    val navController = rememberNavController()
    val books by viewModel.booksFlow.collectAsState(emptyList())
    val persons by viewModel.personsFlow.collectAsState(emptyList())
    val name by viewModel.nameNewBook.collectAsState()
    val desc by viewModel.descNewBook.collectAsState()
    val selectedBook by viewModel.selectedBook.collectAsState()
    val namePerson by viewModel.nameNewPerson.collectAsState()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        Modifier.fillMaxSize(),
        floatingActionButton = {
            AnimatedVisibility(
                currentDestination == "main",
                enter = slideIn { IntOffset(it.width * 2, 0) },
                exit = slideOut { IntOffset(it.width * 2, 0) }
            ) {
                FloatingActionButton({ viewModel.clearAll(); navController.navigate("add") }) {
                    Icon(Icons.Default.Add, null)
                }
            }
        },
        topBar = {
            TopAppBar(
                {
                    AnimatedContent(getTitleScreen(currentDestination)) {
                        Text(it)
                    }
                },
                Modifier.fillMaxWidth(),
                {
                    AnimatedVisibility(
                        currentDestination != "main",
                        enter = expandHorizontally { it / 2 } + scaleIn() + slideIn {
                            IntOffset(
                                -it.width,
                                0
                            )
                        },
                        exit = shrinkHorizontally { 0 } + scaleOut() + slideOut {
                            IntOffset(
                                -it.width,
                                0
                            )
                        }
                    ) {
                        IconButton(
                            navController::popBackStack
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                }
            )
        }
    ) {
        NavHost(navController, "main", Modifier.fillMaxSize().padding(it)) {
            composable("main") {
                MainScreen(
                    books,
                    persons,
                    selectedBook,
                    namePerson,
                    viewModel::setNameNewPerson,
                    viewModel::addPerson,
                    viewModel::setSelectedBook,
                    viewModel::returnBook,
                    viewModel::sendBook
                ) {
                    viewModel.setEditBook(it)
                    navController.navigate("edit")
                }
            }

            composable("edit") {
                EditBookScreen(
                    name,
                    desc,
                    viewModel::setNewName,
                    viewModel::setNewDesc,
                ) {
//                    viewModel.addBook()
                    navController.popBackStack()
                }
            }

            composable("add") {
                AddBookScreen(
                    name,
                    desc,
                    viewModel::setNewName,
                    viewModel::setNewDesc,
                ) {
                    viewModel.addBook()
                    navController.popBackStack()
                }
            }
        }
    }
}

fun getTitleScreen(currentDestination: String?) = when (currentDestination) {
    "edit" -> "Редактирование"
    "add" -> "Добавление"
    else -> "Библиотека"
}
