package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import java.time.DayOfWeek

class WorkoutRepository(application: Application) {
    val workoutDao = Injection.getDatabase(application).workoutDao()

    fun getAllWorkouts() = workoutDao.getAllWorkouts()

    fun getWorkoutsForDay(day: DayOfWeek) = workoutDao.getWorkoutsForDay(day)

    fun insertWorkout(workout: WorkoutEntity) = workoutDao.insertWorkout(workout)

    fun updateWorkout(workout: WorkoutEntity) = workoutDao.updateWorkout(workout)
    fun getExercisesForWorkout(): LiveData<List<ExerciseEntity>> {
        return liveData(Dispatchers.IO) {
            emit(workoutDao.getAllExercises())
        }
    }

}