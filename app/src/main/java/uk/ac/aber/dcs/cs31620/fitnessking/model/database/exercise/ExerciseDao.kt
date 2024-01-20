package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise

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

    // Insert a new exercise
    @Insert
    fun insertExercise(exercise: ExerciseEntity): Long

    // Update an existing exercise
    @Update
    fun updateExercise(exercise: ExerciseEntity)

    // Delete an exercise
    @Delete
    fun deleteExercise(exercise: ExerciseEntity)

    // Get all exercises
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): List<ExerciseEntity>

    // Get a specific exercise by ID
    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId")
    fun getExerciseById(exerciseId: Int): ExerciseEntity?

    // Toggle the favorite status of an exercise
    @Query("UPDATE exercises SET isFavourite = :isFavourite WHERE exerciseId = :exerciseId")
    fun toggleFavorite(exerciseId: Int, isFavourite: Boolean)

}
