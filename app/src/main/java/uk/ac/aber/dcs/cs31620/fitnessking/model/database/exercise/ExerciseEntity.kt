package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This is the entity class for the exercises, it defines the different columns within the exercise
 * table
 * @author Lauren Davis
 */
@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val exerciseId: Int = 0,
    val name: String = "",
    val sets: Int = 0,
    val reps: Int = 0,
    val weight: Int = 0,
    val isDropSet: Boolean = false,
    val image: String = ""
)