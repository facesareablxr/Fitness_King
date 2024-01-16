package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout


import android.app.Application
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection
import java.time.DayOfWeek

class WorkoutRepository(application: Application) {
    private val workoutDao = Injection.getWorkoutDatabase(application).workoutDao()

    fun getAllWorkouts() = workoutDao.getAllWorkouts()

    fun getWorkoutsForDay(day: DayOfWeek) = workoutDao.getWorkoutsForDay(day)

    fun insertWorkout(workout: WorkoutEntity) = workoutDao.insertWorkout(workout)

    fun updateWorkout(workout: WorkoutEntity) = workoutDao.updateWorkout(workout)
}