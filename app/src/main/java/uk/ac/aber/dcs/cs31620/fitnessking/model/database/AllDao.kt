package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek

/*
This is the DAO class which manages things such as insert/update/delete and the queries for searching.
It does not work

@Dao
interface AllDao {
    // Insert functions
    @Insert
    fun insertExercise(exercise: ExerciseEntity)

    @Insert
    fun insertWorkout(workout: WorkoutEntity)

    // Update functions
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateExercise(exercise: ExerciseEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWorkout(workout: WorkoutEntity)

    // Delete functions
    @Delete
    fun deleteExercise(exercise: ExerciseEntity)

    @Delete
    fun deleteWorkout(workout: WorkoutEntity)

    // Queries and transactions
    @Query ("SELECT * FROM exercise_table")
    fun getAllExercises(): LiveData<List<ExerciseEntity>>

    @Query("SELECT * FROM workout_table")
    fun getAllWorkouts(): LiveData<List<WorkoutEntity>>

    @Transaction
    @Query("""SELECT * FROM workout_table""")
    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutEntity>>

    @Query("""SELECT * FROM workout_table WHERE workoutDay = :day""")
    fun getWorkoutsForDay(day: java.time.DayOfWeek): List<WorkoutEntity>

    @Transaction
    @Query("SELECT * FROM workout_table w JOIN exercise_table e ON w.workoutDay = e.exercise_name WHERE w.workoutDay = :date")
    fun getWorkoutWithExercisesForDate(date: String): LiveData<WorkoutWithExercises>

    @Query("SELECT * FROM workoutwithexercisesmap JOIN exercise_table on exerciseId = exercise_name JOIN workout_table on workoutId = workoutDay WHERE workoutId =:workoutDay")
    fun getWorkoutWithExerciseMap(workoutDay: DaysOfWeek): List<WorkoutWithExercises>

}


 */