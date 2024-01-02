package uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.ExerciseDao

/**
 * This is the entity class for the workouts, it defines the different columns within a workout table
 * including the relation to the exercise table.
 */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var day: java.time.DayOfWeek,
    var focus: String,
    var length: Int,
    @ColumnInfo(name = "exercise_id") val exerciseId: Int
) {
    @Ignore var exercises: List<ExerciseEntity> = emptyList()

    suspend fun calculateTotalLength(exerciseDao: ExerciseDao): Int {
        // Fetch exercises if not already populated
        if (exercises.isEmpty()) {
            exercises = getExercises(exerciseDao)
        }

        // Calculate total length
        return exercises.sumOf { it.length }
    }

    private suspend fun getExercises(exerciseDao: ExerciseDao): List<ExerciseEntity> {
        return exerciseDao.getExercisesByWorkoutId(id)
    }

}
