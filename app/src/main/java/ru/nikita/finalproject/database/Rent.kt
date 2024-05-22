package ru.nikita.finalproject.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(DateConverter::class)
data class Rent(
    var userId: Long? = null,
    var dateStart: Date = Date(),
    var dateEnd: Date = Date(),
    @Ignore
    var user: Person? = null,
    @PrimaryKey(true)
    var id: Long = 0
)