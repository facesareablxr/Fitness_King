package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ExerciseViewModel manages the exercises in the database, uses the Dao class
 * @author Lauren Davis
 */
class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExerciseRepository = ExerciseRepository(application)
    private val _exercises = MutableLiveData<List<ExerciseEntity>>()
    val exercises: LiveData<List<ExerciseEntity>> = _exercises

    init {
        refreshExercises()
    }

    private fun refreshExercises() {
        viewModelScope.launch {
            _exercises.value = repository.getAllExercises()
        }
    }

    fun insertExercise(exercise: ExerciseEntity) = viewModelScope.launch {
        repository.insertExercise(exercise)
        refreshExercises()
    }

    fun updateExercise(exercise: ExerciseEntity) = viewModelScope.launch {
        repository.updateExercise(exercise)
        refreshExercises()
    }

    fun deleteExercise(exercise: ExerciseEntity) = viewModelScope.launch {
        repository.deleteExercise(exercise)
        refreshExercises()
    }

    fun toggleFavorite(exerciseId: Int, isFavorite: Boolean) = viewModelScope.launch {
        repository.toggleFavorite(exerciseId, isFavorite)
        refreshExercises()
    }

}