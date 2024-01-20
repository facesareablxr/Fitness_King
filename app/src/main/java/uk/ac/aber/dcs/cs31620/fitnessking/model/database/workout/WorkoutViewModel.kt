package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity


class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private

    val repository: WorkoutRepository = WorkoutRepository(application)

    private val _workoutList = MutableLiveData<List<WorkoutEntity>>()
    val workoutList: LiveData<List<WorkoutEntity>> = _workoutList

    fun insertWorkout(newWorkout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWorkout(newWorkout)
        }
    }

    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWorkout(workout)
        }
    }

    private val _allWorkoutsWithExercises = MutableLiveData<Map<WorkoutEntity, List<ExerciseEntity>>>()
    val allWorkoutsWithExercises: LiveData<Map<WorkoutEntity, List<ExerciseEntity>>> = _allWorkoutsWithExercises

    private val _workoutWithExercises = MutableLiveData<Map<WorkoutEntity, List<ExerciseEntity>>>()
    val workoutWithExercises: LiveData<Map<WorkoutEntity, List<ExerciseEntity>>> = _workoutWithExercises

    private val _exercisesForWorkout = MutableLiveData<List<ExerciseEntity>>()
    val exercisesForWorkout: LiveData<List<ExerciseEntity>> = _exercisesForWorkout

    init {
        loadAllWorkoutsWithExercises()
    }

    private fun loadAllWorkoutsWithExercises() = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.loadAllWorkoutsWithExercises()
        withContext(Dispatchers.Main) {
            _allWorkoutsWithExercises.value = result
        }
    }

    fun loadWorkoutWithExercisesById(workoutId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.loadWorkoutWithExercisesById(workoutId)
        withContext(Dispatchers.Main) {
            _workoutWithExercises.value = result
        }
    }

    fun loadExercisesForWorkout(workoutId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.loadExercisesForWorkout(workoutId)
        withContext(Dispatchers.Main) {
            _exercisesForWorkout.value = result
        }
    }

    fun getExercisesForWorkout(workoutId: Long): LiveData<List<ExerciseEntity>> {
        val exercisesLiveData = MutableLiveData<List<ExerciseEntity>>()

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.loadExercisesForWorkout(workoutId)
            withContext(Dispatchers.Main) {
                exercisesLiveData.value = result
            }
        }
        return exercisesLiveData
    }

}