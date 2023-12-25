package uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "workout_table",
    foreignKeys = [ForeignKey(
        entity = Exercise::class,
        parentColumns = ["id"],
        childColumns = ["exerciseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data

class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val day: String,
    val exerciseFocus: String,
    @ColumnInfo(name = "exerciseId") val exerciseId: Int
)