package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WorkoutRepository = WorkoutRepository(application)

    private val _workouts = MutableLiveData<List<WorkoutEntity>>()
    val workouts: LiveData<List<WorkoutEntity>> = _workouts

    var workoutList: List<WorkoutEntity> = repository.getAllWorkouts()
        private set

    private val _workoutsForDay = MutableLiveData<List<WorkoutEntity>>()
    val workoutsForDay: LiveData<List<WorkoutEntity>> = _workoutsForDay

    private val _currentWorkoutDay = MutableLiveData<DayOfWeek>(LocalDate.now().dayOfWeek) // Current day
    private val currentWorkoutDay: LiveData<java.time.DayOfWeek> = _currentWorkoutDay

    // Inserting workout to database
    fun insertWorkout(workout: WorkoutEntity) = viewModelScope.launch {
        repository.insertWorkout(workout)
        refreshWorkouts()
    }

    // Updating workouts in database
    fun updateWorkout(workout: WorkoutEntity) = viewModelScope.launch {
        repository.updateWorkout(workout)
        refreshWorkouts()
    }
    init {
        refreshWorkouts()
    }

    private fun refreshWorkouts() {
        viewModelScope.launch {
            _workouts.value = repository.getAllWorkouts()
        }
    }

    fun refreshWorkoutsForDay() {
        viewModelScope.launch {
            _workoutsForDay.value = repository.getWorkoutsForDay(currentWorkoutDay.value!!)
        }
    }

    fun setCurrentWorkoutDay(dayOfWeek: DaysOfWeek) {
        val convertedDay = when (dayOfWeek) {
            DaysOfWeek.Monday -> DayOfWeek.MONDAY
            DaysOfWeek.Tuesday -> DayOfWeek.TUESDAY
            DaysOfWeek.Wednesday -> DayOfWeek.WEDNESDAY
            DaysOfWeek.Thursday -> DayOfWeek.THURSDAY
            DaysOfWeek.Friday -> DayOfWeek.FRIDAY
            DaysOfWeek.Saturday -> DayOfWeek.SATURDAY
            DaysOfWeek.Sunday -> DayOfWeek.SUNDAY
        }
        _currentWorkoutDay.value = convertedDay
        refreshWorkoutsForDay()
    }

}

