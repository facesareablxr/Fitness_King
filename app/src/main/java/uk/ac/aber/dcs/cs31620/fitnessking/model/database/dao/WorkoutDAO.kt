package uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.Exercise
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.Workout

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_table ORDER BY day ASC")
    fun getAllWorkouts(): Flow<List<Workout>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)
    @Update
    suspend fun updateWorkout(workout: Workout)
    @Delete
    suspend fun deleteWorkout(workout: Workout)
    @Transaction
    @Query("SELECT * FROM workout_table")
    fun getWorkoutsWithExercises(): Flow<List<WorkoutWithExercise>>
}

data class WorkoutWithExercise(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id"
    )
    val exercise: Exercise
)