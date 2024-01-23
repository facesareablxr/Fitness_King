package uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses

/**
 * Workout with exercises data class, used mainly in WorkoutWithExerciseManagement
 */
data class WorkoutWithExercises(
    val dayOfWeek: String,
    val focus: String,
    var exercises: List<Exercise>
)