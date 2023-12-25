package uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")

data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sets") val sets: Int,
    @ColumnInfo(name = "reps") val reps: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "focus") val focus: String,
    @ColumnInfo(name = "dropset") val dropset: Boolean,
    @ColumnInfo(name = "image") val image: String
)