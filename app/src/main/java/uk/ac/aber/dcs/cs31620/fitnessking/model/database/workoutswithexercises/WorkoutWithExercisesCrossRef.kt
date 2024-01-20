package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.Entity
import androidx.room.TypeConverters

@Entity(primaryKeys = ["workoutId", "exerciseId"], tableName = "workoutwithexercises")
data class WorkoutWithExercisesCrossRef(
    val workoutId: Long,

    @TypeConverters(ExerciseIdsConverter::class)
    val exerciseId: String
)





