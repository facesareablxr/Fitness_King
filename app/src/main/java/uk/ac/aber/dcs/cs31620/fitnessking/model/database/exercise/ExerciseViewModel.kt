package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

/**
 * ExerciseViewModel manages the exercises in the database, uses the Dao class
 * @author Lauren Davis
 */
class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExerciseRepository = ExerciseRepository(application)
    // LiveData for observing all exercises
    val allExercises: LiveData<List<ExerciseEntity>> = repository.getAllExercises()

    // Function to insert a new exercise
    fun insertExercise(exercise: ExerciseEntity): Boolean {
        repository.insertExercise(exercise)
        return true // Return result from repository
    }
    // Function to update an existing exercise
    fun updateExercise(exercise: ExerciseEntity) {
        repository.updateExercise(exercise)
    }
    // Function to delete an exercise
    fun deleteExercise(exercise: ExerciseEntity) {
        repository.deleteExercise(exercise)
    }
    // Function to calculate the length of the workout, from the list of exercises



}