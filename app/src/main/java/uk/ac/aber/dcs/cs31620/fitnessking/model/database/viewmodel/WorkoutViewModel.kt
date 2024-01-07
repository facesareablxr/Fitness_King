package uk.ac.aber.dcs.cs31620.fitnessking.model.database.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.FitnessRepository
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus
import java.time.LocalDate
import java.time.DayOfWeek

abstract class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FitnessRepository = FitnessRepository(application)

    var allWorkouts: LiveData<List<WorkoutEntity>> = repository.getAllWorkouts()
        private set

    private var workoutForToday: WorkoutEntity by mutableStateOf(
        WorkoutEntity(
            day = DaysOfWeek.valueOf(LocalDate.now().dayOfWeek.toString()),
            exerciseIds = mutableListOf(),
            focus = Focus.Arms,
            length = 60
        )
    )

    suspend fun getWorkoutsForDay(day: DayOfWeek): List<WorkoutEntity> {
        return repository.getWorkoutsForDay(day)
    }

    fun insertWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWorkout(workout)
        }
    }

    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWorkout(workout)
        }
    }

    fun updateWorkoutForToday(workout: WorkoutEntity) {
        workoutForToday = workout
    }

}