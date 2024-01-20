package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout


import android.app.Application
import androidx.lifecycle.LiveData
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercises
import java.time.LocalDate

class WorkoutRepository(application: Application) {
    private val allDao = Injection.getDatabase(application).allDao()

    fun getAllWorkouts() = allDao.getAllWorkouts()

    fun getCurrentWorkout(): WorkoutEntity? {
        val currentDay = LocalDate.now().dayOfWeek
        return allDao.getCurrentWorkout(currentDay)
    }

    fun insertWorkout(workout: WorkoutEntity) = allDao.insertWorkout(workout)

    fun updateWorkout(workout: WorkoutEntity) = allDao.updateWorkout(workout)

    fun loadAllWorkoutsWithExercises(): Map<WorkoutEntity, List<ExerciseEntity>> =
        allDao.loadAllWorkoutsWithExercises()

    fun loadWorkoutWithExercisesById(workoutId: Long): Map<WorkoutEntity, List<ExerciseEntity>> =
        allDao.loadWorkoutWithExercisesById(workoutId)

    fun loadExercisesForWorkout(workoutId: Long): List<ExerciseEntity> =
        allDao.loadExercisesForWorkout(workoutId)
}
