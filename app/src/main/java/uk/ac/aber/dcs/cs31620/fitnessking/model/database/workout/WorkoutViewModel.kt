package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WorkoutRepository = WorkoutRepository(application)
    var workoutList: LiveData<List<WorkoutEntity>> = repository.getAllWorkouts()
        private set

    fun insertWorkout(newWorkout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insertWorkout(newWorkout)
        }
    }
}