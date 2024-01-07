package uk.ac.aber.dcs.cs31620.fitnessking.model.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.ExerciseDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.ExerciseEntity

/**
 * ExerciseViewModel manages the exercises in the database, uses the Dao class
 * @author Lauren Davis
 */
class ExerciseViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {
    // LiveData for observing all exercises
    val allExercises: LiveData<List<ExerciseEntity>> = exerciseDao.getAllExercises()
    // Function to insert a new exercise
    suspend fun insertExercise(exercise: ExerciseEntity) {
        exerciseDao.insertExercise(exercise)
    }
    // Function to update an existing exercise
    suspend fun updateExercise(exercise: ExerciseEntity) {
        exerciseDao.updateExercise(exercise)
    }
    // Function to delete an exercise
    suspend fun deleteExercise(exercise: ExerciseEntity) {
        exerciseDao.deleteExercise(exercise)
    }

    //Function to get exercises by their ID
    fun getExerciseById(id: Int): ExerciseEntity {
        return exerciseDao.getExerciseById(id)
    }

    // Function to calculate the length of the workout, from the list of exercises
    fun calculateLength(selectedExerciseIds: List<Int>, exerciseDao: ExerciseDao): Int {
        val selectedExercises = selectedExerciseIds.map { exerciseDao.getExerciseById(it) }
        return selectedExercises.sumOf { it.length }
    }


}