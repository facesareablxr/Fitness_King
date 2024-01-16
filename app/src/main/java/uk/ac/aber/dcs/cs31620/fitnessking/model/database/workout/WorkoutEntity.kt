package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.ExerciseIdsConverter
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus

/**
 * This is the entity class for the workouts, it defines the different columns within a workout table
 * including the relation to the exercise table.
 */
@TypeConverters(ExerciseIdsConverter::class)
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) var workoutId: Int = 0,
    var day: DaysOfWeek = DaysOfWeek.Monday,
    var focus: Focus = Focus.Arms,
    var length: Int = 0,
)
    @Ignore var exerciseEntities: List<ExerciseEntity> = emptyList()

/*    fun calculateTotalLength(exerciseDao: ExerciseDao): Int {
        // Fetch exercises if not already populated
        if (exerciseEntities.isEmpty()) {
            exerciseEntities = getExercises(exerciseDao)
        }*//*

        // Calculate total length
        return exerciseEntities.sumOf { it.length }
    }*/