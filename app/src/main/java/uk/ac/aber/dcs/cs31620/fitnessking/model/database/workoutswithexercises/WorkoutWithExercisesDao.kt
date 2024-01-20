package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.*

@Dao
abstract class WorkoutExerciseDao {
    @Insert
    abstract fun insert(workoutExerciseMapDao: WorkoutWithExercises): Long
    @Query("SELECT * FROM workoutwithexercises")
    abstract fun getAllWorkoutExercise(): List<WorkoutWithExercises>
    @Transaction
    @Query("SELECT * FROM workouts")
    abstract fun getAllWorkoutsWithExercises(): List<WorkoutWithExercises>

}
