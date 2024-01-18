package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.Entity

@Entity(primaryKeys = ["workoutId", "exerciseId"], tableName = "workoutwithexercises")
data class WorkoutWithExercises(
    val workoutId: Int,
    val exerciseId: Int
)