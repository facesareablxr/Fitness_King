package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Dao interface for the exercises, handles the manipulation of data
 * @author Lauren Davis
 */
@Dao
interface ExerciseDao {
    /**
     * General commands
     */
    // Adds a new exercise into the database
    @Insert
    fun insertExercise(exercise: ExerciseEntity)
    // Updates the exercise within the database
    @Update
    fun updateExercise(exercise: ExerciseEntity)
    // Removes the exercise from the database
    @Delete
    fun deleteExercise(exercise: ExerciseEntity)

    /**
     * Queries
     */
    // Gets all the exercises by their id
    @Query("SELECT * FROM exercises WHERE exerciseId = :id")
    fun getExerciseById(id: Int): ExerciseEntity
    // Gets all exercises from the database as a LiveData object
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): LiveData<List<ExerciseEntity>>


}

