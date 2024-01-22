package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

/*
This is the workout view model class, it handles the operations such as search, insert and update
through the calling of the repository. This does not work.

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FitnessRepository = FitnessRepository(application)

    private val _workoutList = MutableLiveData<List<WorkoutEntity>>()
    val workoutList: LiveData<List<WorkoutEntity>> = _workoutList

    // Function to insert workout
    fun insertWorkout(newWorkout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWorkout(newWorkout)
        }
    }

    // Function to update workout
    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: WorkoutEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteWorkout(workout)
        }
    }


    val allWorkouts: LiveData<List<WorkoutEntity>> = repository.allWorkouts

    val todaysWorkoutWithExercises: LiveData<WorkoutWithExercises> = repository.getTodaysWorkoutWithExercises(
        day = LocalDate.now().dayOfWeek.name)

}


 */