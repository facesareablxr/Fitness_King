package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout


import android.app.Application
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek

class WorkoutRepository(application: Application) {
    private val workoutDao = Injection.getDatabase(application).workoutDao()

    fun getAllWorkouts() = workoutDao.getAllWorkouts()

    fun insertWorkout(workout: WorkoutEntity) = workoutDao.insertWorkout(workout)

    fun updateWorkout(workout: WorkoutEntity) = workoutDao.updateWorkout(workout)

    fun getWorkoutsByDay(day: DaysOfWeek) = workoutDao.getWorkoutsByDay(day)

    fun getWorkoutsForDay(day: java.time.DayOfWeek) = workoutDao.getWorkoutsForDay(day)


}