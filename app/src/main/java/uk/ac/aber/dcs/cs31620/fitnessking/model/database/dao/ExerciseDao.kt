package uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.ExerciseEntity

/**
 * Dao interface for the exercises, handles the manipulation of data
 * @author Lauren Davis
 */
@Dao
interface ExerciseDao {
    // Gets all exercises from the database as a LiveData object
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<ExerciseEntity>>
    // Adds a new exercise into the database
    @Insert
    suspend fun insertExercise(exercise: ExerciseEntity)
    // Updates the exercise within the database
    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)
    // Removes the exercise from the database
    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercises WHERE id = :workoutId")
    suspend fun getExercisesByWorkoutId(workoutId: Int): List<ExerciseEntity>

}

