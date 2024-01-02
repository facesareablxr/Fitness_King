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
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.RoomDB
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.WorkoutDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.WorkoutEntity
import java.time.LocalDate
import java.time.DayOfWeek

abstract class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutDao: WorkoutDao = RoomDB.getInstance(application).workoutDao()

    var allWorkouts: LiveData<List<WorkoutEntity>> = workoutDao.getAllWorkouts()
        private set

    var workoutForToday: WorkoutEntity by mutableStateOf(
        WorkoutEntity(
            day = LocalDate.now().dayOfWeek,
            exerciseId = 0,
            focus = "Legs",
            length = 60
        )
    )
    private set

    suspend fun getWorkoutsForDay(day: DayOfWeek): List<WorkoutEntity> {
        return workoutDao.getWorkoutsForDay(day)
    }

    fun insertWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.insertWorkout(workout)
        }
    }

    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.deleteWorkout(workout)
        }
    }

    fun updateWorkoutForToday(workout: WorkoutEntity) {
        workoutForToday = workout
    }
}