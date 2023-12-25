package uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.Exercise

@Dao
interface ExerciseDAO {
    @Query("SELECT * FROM exercise_table ORDER BY name ASC")
    fun getAllExercises(): Flow<List<Exercise>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)
    @Update
    suspend fun updateExercise(exercise: Exercise)
    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}