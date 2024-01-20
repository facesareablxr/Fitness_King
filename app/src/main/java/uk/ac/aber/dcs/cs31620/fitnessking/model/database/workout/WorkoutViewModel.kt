package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercises


class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WorkoutRepository = WorkoutRepository(application)

    var workoutList: LiveData<List<WorkoutEntity>> = repository.getAllWorkouts()
        private set

    fun insertWorkout(newWorkout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWorkout(newWorkout)
        }
    }

    private val _currentWorkout = MutableLiveData<WorkoutEntity?>()
    val currentWorkout: LiveData<WorkoutEntity?> = _currentWorkout

    fun fetchCurrentWorkout() = viewModelScope.launch {
        _currentWorkout.value = repository.getCurrentWorkout()
    }

    private val _allWorkoutsWithExercises = MutableLiveData<List<WorkoutWithExercises>>(emptyList())
    val allWorkoutsWithExercises: LiveData<List<WorkoutWithExercises>> = _allWorkoutsWithExercises

    fun fetchAllWorkoutsWithExercises() = viewModelScope.launch {
        _allWorkoutsWithExercises.value = repository.getAllWorkoutsWithExercises().value
    }

    fun updateWorkout(workout: WorkoutEntity) {
        repository.updateWorkout(workout)
    }

}