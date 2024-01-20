package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import android.app.Application
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection

class WorkoutWithExercisesRepository (application: Application) {
    private val workoutWithExercisesDao = Injection.getDatabase(application).workoutWithExercisesDao()

    fun insertAll(workoutWithExercises: WorkoutWithExercises) {
        workoutWithExercisesDao.insertWorkoutWithExercises(workoutWithExercises)
    }

    fun addExerciseToWorkout(workoutWithExercisesCrossRef: WorkoutWithExercisesCrossRef) {
        workoutWithExercisesDao.addExerciseToWorkout(workoutWithExercisesCrossRef)
    }

    fun removeExerciseFromWorkout(workoutWithExercisesCrossRef: WorkoutWithExercisesCrossRef) {
        workoutWithExercisesDao.removeExerciseFromWorkout(workoutWithExercisesCrossRef)
    }

    fun getAllWorkoutsWithExercises(): List<WorkoutWithExercises> {
        return workoutWithExercisesDao.getAllWorkoutsWithExercises()
    }

    fun getWorkoutWithExercisesById(workoutId: Long): WorkoutWithExercises? {
        return workoutWithExercisesDao.getWorkoutWithExercisesById(workoutId)
    }

    fun deleteWorkoutWithExercises(workoutId: Long) {
        workoutWithExercisesDao.deleteWorkoutWithExercises(workoutId)
    }

    fun getByDayOfWeek(dayOfWeek: Int): List<WorkoutWithExercises> {
        return workoutWithExercisesDao.getByDayOfWeek(dayOfWeek)
    }

    fun hasWorkoutForDay(dayOfWeek: Int): Boolean {
        return workoutWithExercisesDao.hasWorkoutForDay(dayOfWeek)
    }
}