package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorkoutWithExercisesDao {

    @Delete
    fun delete(workoutWithExercise: WorkoutWithExercises)

    @Query("SELECT * FROM WorkoutWithExercises")
    fun getAllWorkoutWithExercises(): LiveData<List<WorkoutWithExercises>>

    @Query("DELETE FROM workoutwithexercises WHERE workoutId = :workoutId")
    fun clearExercisesForWorkout(workoutId: Int)

    @Query("DELETE FROM workoutwithexercises WHERE exerciseId = :exerciseId")
    fun clearWorkoutsForExercise(exerciseId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg workoutWithExercises: WorkoutWithExercises)

    @Query("SELECT * FROM WorkoutWithExercises WHERE workoutId = :workoutId")
    fun getExercisesForWorkout(workoutId: Int): LiveData<List<WorkoutWithExercises>>
}