package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import java.time.LocalDate

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WorkoutRepository = WorkoutRepository(application)
    private val workoutDao = repository.workoutDao

    var workoutList: LiveData<List<WorkoutEntity>> = repository.getAllWorkouts()
        private set

    fun insertWorkout(newWorkout: WorkoutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWorkout(newWorkout)
        }
    }

    val currentWorkout: LiveData<WorkoutEntity?> = liveData(Dispatchers.IO) {
        emit(workoutDao.getWorkoutForToday())
    }

    private fun WorkoutDao.getWorkoutForToday(): WorkoutEntity? {
        val today = LocalDate.now().dayOfWeek
        return getWorkoutsForDay(today).firstOrNull()
    }

    fun getExercisesForWorkout(workout: WorkoutEntity): LiveData<List<ExerciseEntity>> {
        return liveData(Dispatchers.IO) {
            emit(workoutDao.getExercisesForWorkout(workout.workoutId))
        }
    }
}