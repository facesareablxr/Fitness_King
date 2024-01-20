package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus

/**
 * This is the entity class for the workouts, it defines the different columns within a workout table
 * including the relation to the exercise table.
 */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) var workoutId: Int = 0,
    var day: DaysOfWeek = DaysOfWeek.Monday,
    var focus: Focus = Focus.Arms,
    var length: Int = 0,
    val restTime: Int = 0
)