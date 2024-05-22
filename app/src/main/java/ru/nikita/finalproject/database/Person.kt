package ru.nikita.finalproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    var username: String
){
    @PrimaryKey(true)
    var id: Long = 0
}