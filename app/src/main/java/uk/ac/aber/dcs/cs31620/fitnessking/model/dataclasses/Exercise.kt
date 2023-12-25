package uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Int,
    val focus: String,
    val dropSet: Boolean,
    val image: String
)
