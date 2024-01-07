package uk.ac.aber.dcs.cs31620.fitnessking.model.database


import android.app.Application
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.viewmodel.Injection
import java.time.DayOfWeek

class FitnessRepository(application: Application) {
    private val exerciseDao = Injection.getDatabase(application).ExerciseDao()
    private val workoutDao = Injection.getDatabase(application).WorkoutDao()

    // Exercise CRUD Functions
    fun getAllExercises() = exerciseDao.getAllExercises()

    suspend fun insertExercise(exercise: ExerciseEntity) = exerciseDao.insertExercise(exercise)

    suspend fun updateExercise(exercise: ExerciseEntity) = exerciseDao.updateExercise(exercise)

    suspend fun deleteExercise(exercise: ExerciseEntity) = exerciseDao.deleteExercise(exercise)

    // Workout CRUD Functions

    fun getAllWorkouts() = workoutDao.getAllWorkouts()

    suspend fun getWorkoutsForDay(day: DayOfWeek) = workoutDao.getWorkoutsForDay(day)

    suspend fun insertWorkout(workout: WorkoutEntity) = workoutDao.insertWorkout(workout)

    suspend fun updateWorkout(workout: WorkoutEntity) = workoutDao.updateWorkout(workout)

    suspend fun deleteWorkout(workout: WorkoutEntity) = workoutDao.deleteWorkout(workout)
}