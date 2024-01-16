package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.WorkoutWithExercises

@Dao
interface WorkoutWithExercisesDao {
    @Transaction
    @Query("SELECT * FROM workouts")
    fun getWorkoutWithExercises(): List<WorkoutWithExercises>
}