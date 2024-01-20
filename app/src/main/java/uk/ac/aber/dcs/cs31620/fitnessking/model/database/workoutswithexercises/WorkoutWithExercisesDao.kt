package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.*

@Dao
interface WorkoutWithExercisesDao {
    //Insert a workout with its exercises
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkoutWithExercises(vararg workoutWithExercises: WorkoutWithExercises)

    // Inserts a workout and exercises to cross ref table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkoutWithExercisesCrossRef(workoutWithExercisesCrossRef: List<WorkoutWithExercisesCrossRef>)

    // Add an exercise to an existing workout
    @Insert
    fun addExerciseToWorkout(workoutWithExercisesCrossRef: WorkoutWithExercisesCrossRef)

    // Remove an exercise from a workout
    @Delete
    fun removeExerciseFromWorkout(workoutWithExercisesCrossRef: WorkoutWithExercisesCrossRef)

    // Get all workouts with exercises
    @Transaction
    @Query("SELECT * FROM workouts INNER JOIN workoutwithexercises ON workouts.workoutId = workoutwithexercises.workoutId")
    fun getAllWorkoutsWithExercises(): List<WorkoutWithExercises>

    // Get a workout with its exercises by workout ID
    @Transaction
    @Query("SELECT * FROM workoutwithexercises INNER JOIN workouts ON workoutwithexercises.workoutId = workouts.workoutId WHERE workoutwithexercises.workoutId = :workoutId")
    fun getWorkoutWithExercisesById(workoutId: Long): WorkoutWithExercises?

    // Delete a workout and its associated exercises
    @Transaction
    @Query("DELETE FROM workouts WHERE workoutId = :workoutId")
    fun deleteWorkoutWithExercises(workoutId: Long)

    // Delete workout and its exercises fro cross ref
    @Query("DELETE FROM workoutwithexercises WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutExerciseCrossRefs(workoutId: Long)

    // Gets exercises by workout day
    @Transaction
    @Query("SELECT * FROM workoutwithexercises INNER JOIN workouts ON workoutwithexercises.workoutId = workouts.workoutId WHERE workouts.day = :dayOfWeek")
    fun getByDayOfWeek(dayOfWeek: Int): List<WorkoutWithExercises>


    // Checks if there is a workout scheduled for a chosen day of the week
    @Query("SELECT EXISTS(SELECT 1 FROM workouts WHERE day = :dayOfWeek)")
    fun hasWorkoutForDay(dayOfWeek: Int): Boolean
}