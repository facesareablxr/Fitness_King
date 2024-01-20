package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity

data class WorkoutWithExercises(
    @Embedded val workoutEntity: WorkoutEntity,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "exerciseId",
        associateBy = Junction(WorkoutWithExercisesCrossRef::class)
    )
    val exercises: List<ExerciseEntity>
)
