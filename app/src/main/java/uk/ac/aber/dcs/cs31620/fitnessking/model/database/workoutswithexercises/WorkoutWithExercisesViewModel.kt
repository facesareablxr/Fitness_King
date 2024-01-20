package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WorkoutWithExercisesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WorkoutWithExercisesRepository = WorkoutWithExercisesRepository(application)

    private val _workouts = MutableLiveData<List<WorkoutWithExercises>>()
    val workouts: LiveData<List<WorkoutWithExercises>> = _workouts

    private val _workoutsForDay = MutableLiveData<List<WorkoutWithExercises>>()
    val workoutsForDay: LiveData<List<WorkoutWithExercises>> = _workoutsForDay

    private val _currentWorkout = MutableLiveData<WorkoutWithExercises?>()
    val currentWorkout: LiveData<WorkoutWithExercises?> = _currentWorkout

    init {
        refreshWorkouts()
    }

    private fun refreshCurrentWorkout() {
        viewModelScope.launch {
            val currentWorkout = _currentWorkout.value ?: return@launch
            val currentWorkoutId = currentWorkout.workoutEntity.workoutId
            _currentWorkout.value = repository.getWorkoutWithExercisesById(currentWorkoutId)
        }
    }

    private fun refreshWorkouts() {
        viewModelScope.launch {
            _workouts.value = repository.getAllWorkoutsWithExercises()
        }
    }

    fun refreshWorkoutsForDay(dayOfWeek: Int) {
        viewModelScope.launch {
            _workoutsForDay.value = repository.getByDayOfWeek(dayOfWeek)
        }
    }

    fun setCurrentWorkout(workoutId: Long) {
        viewModelScope.launch {
            _currentWorkout.value = repository.getWorkoutWithExercisesById(workoutId)
        }
    }

    fun insertWorkoutWithExercises(workoutWithExercises: WorkoutWithExercises) = viewModelScope.launch {
        repository.insertAll(workoutWithExercises)
        refreshWorkouts()
    }

    fun addExerciseToWorkout(workoutId: Long, exerciseId: String) = viewModelScope.launch {
        val workoutWithExercisesCrossRef = WorkoutWithExercisesCrossRef(workoutId, exerciseId)
        repository.addExerciseToWorkout(workoutWithExercisesCrossRef)
        refreshCurrentWorkout()
    }

    fun removeExerciseFromWorkout(workoutId: Long, exerciseId: String) = viewModelScope.launch {
        val workoutWithExercisesCrossRef = WorkoutWithExercisesCrossRef(workoutId, exerciseId)
        repository.removeExerciseFromWorkout(workoutWithExercisesCrossRef)
        refreshCurrentWorkout()
    }

    fun updateWorkoutWithExercises(workoutWithExercises: WorkoutWithExercises) = viewModelScope.launch {
        // Implement logic to update the workout and its exercises
        refreshWorkouts()
    }

    fun deleteWorkoutWithExercises(workoutId: Long) = viewModelScope.launch {
        repository.deleteWorkoutWithExercises(workoutId)
        refreshWorkouts()
    }
}

