package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.Entity
import androidx.room.ForeignKey
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity

@Entity(
    primaryKeys = ["workout_Id","exercise_Id"],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["workoutId"],
            childColumns = ["workout_Id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(entity = ExerciseEntity::class,parentColumns = ["exerciseId"],childColumns = ["exercise_Id"])
    ]
)
data class WorkoutWithExercises(
    val workout_Id: Long,
    var exercise_Id: Long
)