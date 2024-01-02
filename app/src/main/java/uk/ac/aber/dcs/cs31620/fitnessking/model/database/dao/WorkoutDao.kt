package uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.WorkoutEntity

/**
 * Dao interface for the workouts, handles the manipulation of data
 * @author Lauren Davis
 */
@Dao
interface WorkoutDao {
    // Gets all workouts from the database
    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): LiveData<List<WorkoutEntity>>
    // Gets all workouts from the database for a specific day
    @Query("SELECT * FROM workouts WHERE day = :day")
    suspend fun getWorkoutsForDay(day: java.time.DayOfWeek): List<WorkoutEntity>
    // Inserts a new workout into a database
    @Insert
    suspend fun insertWorkout(workout: WorkoutEntity)
    // Updates a workout within the database
    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)
    // Deletes a workout from the table
    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)
}