package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Exercise
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus



/*
 * This is the entity class for the workouts, it defines the different columns within a workout table
 * including the relation to the exercise table. This does not work.

@Entity(tableName = "workout_table")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = false)
    var workoutDay: DaysOfWeek,
    var focus: Focus = Focus.Arms,
    var length: Int = 0,
    val restTime: Int = 0
)

 */
