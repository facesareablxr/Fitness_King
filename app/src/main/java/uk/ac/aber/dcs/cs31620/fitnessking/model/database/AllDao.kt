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
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM workouts JOIN workoutwithexercises ON workouts.workoutId = workoutwithexercises.workout_Id JOIN exercises ON workoutwithexercises.exercise_Id = exercises.exerciseId")
    abstract fun loadAllWorkoutsWithExercises(): Map<WorkoutEntity, List<ExerciseEntity>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT workouts.workoutId, workouts.day, workouts.focus, workouts.length, workouts.restTime, exercises.exerciseId, exercises.name, exercises.sets, exercises.reps, exercises.weight, exercises.isDropSet, exercises.image, exercises.isFavourite, exercises.length FROM workouts JOIN workoutwithexercises ON workouts.workoutId = workoutwithexercises.workout_Id JOIN exercises ON workoutwithexercises.exercise_Id = exercises.exerciseId WHERE workouts.workoutId = :workoutId")
    abstract fun loadWorkoutWithExercisesById(workoutId: Long): Map<WorkoutEntity, List<ExerciseEntity>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT exercises.* FROM workoutwithexercises JOIN exercises ON workoutwithexercises.exercise_Id = exercises.exerciseId WHERE workoutwithexercises.workout_Id = :workoutId")
    abstract fun loadExercisesForWorkout(workoutId: Long): List<ExerciseEntity>

}
