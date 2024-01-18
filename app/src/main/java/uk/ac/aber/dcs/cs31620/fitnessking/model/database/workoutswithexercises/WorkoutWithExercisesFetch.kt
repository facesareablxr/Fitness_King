package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity

data class WorkoutWithExercisesFetch(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "exerciseId",
        associateBy = Junction(WorkoutWithExercises::class, parentColumn = "workoutId", entityColumn = "exerciseId")
    )
    val exercises: List<ExerciseEntity>
)