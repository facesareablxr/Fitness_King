package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercises

/**
 * Dao interface for the workouts, handles the manipulation of data
 * @author Lauren Davis
 */
@Dao
interface WorkoutDao {
    // Gets all workouts from the database
    @Query("""SELECT * FROM workouts""")
    fun getAllWorkouts(): LiveData<List<WorkoutEntity>>
    // Gets all workouts from the database for a specific day
    @Query("""SELECT * FROM workouts WHERE day = :day""")
    fun getWorkoutsForDay(day: java.time.DayOfWeek): List<WorkoutEntity>
    // Inserts a new workout into a database
    @Insert
    fun insertWorkout(workout: WorkoutEntity)
    // Updates a workout within the database
    @Update
    fun updateWorkout(workout: WorkoutEntity)
    // Deletes a workout from the table
    @Delete
    fun deleteWorkout(workout: WorkoutEntity)

    @Query("SELECT * FROM exercises WHERE exerciseId IN (:exerciseIds)")
    fun getExercisesByIds(exerciseIds: List<Int>): List<ExerciseEntity>

    @Transaction
    @Query("SELECT exercises.exerciseId, exercises.name, exercises.sets, exercises.reps, exercises.weight, exercises.isDropSet, exercises.image FROM exercises INNER JOIN workoutwithexercises ON exercises.exerciseId = workoutwithexercises.exerciseId WHERE workoutwithexercises.workoutId = :workoutId")
    suspend fun getExercisesForWorkout(workoutId: Int): List<ExerciseEntity>

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<ExerciseEntity>

}
