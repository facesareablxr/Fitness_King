package uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses

data class WorkoutWithExercises(
    val dayOfWeek: String,
    val focus: String,
    var exercises: List<Exercise>
)