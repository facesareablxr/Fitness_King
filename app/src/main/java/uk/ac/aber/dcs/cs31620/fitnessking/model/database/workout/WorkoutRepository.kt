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

    fun getAllWorkoutsWithExercises(): LiveData<List<WorkoutWithExercises>> =
        allDao.getAllWorkoutsWithExercises()

    fun getWorkoutWithExercises(workoutId: Long): LiveData<WorkoutWithExercises> {
        return allDao.getWorkoutWithExercises(workoutId)
    }

    fun getExercisesForWorkout(workoutId: Long): LiveData<List<ExerciseEntity>> {
        return allDao.getExercisesForWorkout(workoutId)
    }
}
