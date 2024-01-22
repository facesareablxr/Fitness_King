package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/*
This is the workoutwithexercises class, it has relations between workout entity and exercise entity.
This does not work.


data class WorkoutWithExercises(
    @Embedded
    val workout: WorkoutEntity,
   // @Relation(entityColumn = "exerciseName", parentColumn = "exerciseName")
    @Embedded
    val exercises: ExerciseEntity
)


 */
