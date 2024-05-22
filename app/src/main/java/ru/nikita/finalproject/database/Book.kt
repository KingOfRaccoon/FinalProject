package ru.nikita.finalproject.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Rent::class,
            parentColumns = ["id"],
            childColumns = ["rentId"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Book(
    var name: String = "",
    var description: String = "",
    var rentId: Int? = null,
    @Ignore
    var rent: Rent? = null,
    @PrimaryKey(true)
    var id: Int = 0
)
