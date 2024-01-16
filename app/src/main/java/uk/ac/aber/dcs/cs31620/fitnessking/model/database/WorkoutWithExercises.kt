package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import androidx.room.Embedded
import androidx.room.Relation
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity

data class WorkoutWithExercises(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "exerciseId"
    )
    val exercises: List<ExerciseEntity>
)