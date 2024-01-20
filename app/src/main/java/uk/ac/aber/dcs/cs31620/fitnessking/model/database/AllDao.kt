package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercises
import java.time.DayOfWeek

@Dao
abstract class AllDao {
    //Insert Functions
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertExercise(exercise: ExerciseEntity): Long
    @Insert
    abstract fun insertWorkout(workout: WorkoutEntity): Long
    @Insert
    abstract fun insertWorkoutWithExercises(workoutWithExercises: WorkoutWithExercises): Long

    //Update Functions
    @Update
    abstract fun updateExercise(exercise: ExerciseEntity)
    @Update
    abstract fun updateWorkout(workout: WorkoutEntity)


    //Delete Functions
    @Delete
    abstract fun deleteExercise(exercise: ExerciseEntity)
    @Delete
    abstract fun deleteWorkout(workout: WorkoutEntity)



    //Queries
    @Query("SELECT * FROM workouts")
    abstract fun getAllWorkouts(): LiveData<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE workouts.workoutId=:workoutId")
    abstract fun getWorkoutById(workoutId: Long): WorkoutEntity

    @Query("SELECT * FROM exercises")
    abstract fun getAllExercises(): LiveData<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE exercises.exerciseId=:exerciseId")
    abstract fun getExerciseById(exerciseId: Long): ExerciseEntity

    @Query("SELECT * FROM workoutwithexercises")
    @Transaction
    abstract fun getAllWorkoutsWithExercises(): LiveData<List<WorkoutWithExercises>>

    @Query("SELECT * FROM workouts WHERE day = :currentDay")
    abstract fun getCurrentWorkout(currentDay: DayOfWeek): WorkoutEntity?

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    abstract fun getWorkoutWithExercises(workoutId: Long): LiveData<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM exercises WHERE exerciseId IN (SELECT exerciseId FROM workoutwithexercises WHERE workout_Id = :workoutId)")
    abstract fun getExercisesForWorkout(workoutId: Long): LiveData<List<ExerciseEntity>>

}
