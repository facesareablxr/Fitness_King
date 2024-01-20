package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import androidx.room.*
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import java.time.DayOfWeek

/**
 * Dao interface for the workouts, handles the manipulation of data
 * @author Lauren Davis
 */
@Dao
interface WorkoutDao {

    // Inserts a new workout into a database
    @Insert
    fun insertWorkout(workout: WorkoutEntity): Long

    // Update an existing workout
    @Update
    fun updateWorkout(workout: WorkoutEntity)

    // Delete a workout
    @Delete
    fun deleteWorkout(workout: WorkoutEntity)

    // Get all workouts
    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): List<WorkoutEntity>

    // Gets workouts by day
    @Query("SELECT * FROM workouts WHERE day = :dayOfWeek")
    fun getWorkoutsByDay(dayOfWeek: DaysOfWeek): List<WorkoutEntity>

    // Gets workout for current day
    @Query("""SELECT * FROM workouts WHERE day = :day""")
    fun getWorkoutsForDay(day: DayOfWeek): List<WorkoutEntity>

    // Gets days with workouts
    @Query("SELECT DISTINCT day FROM workouts")
    fun getDaysWithWorkouts(): List<DaysOfWeek>

}
