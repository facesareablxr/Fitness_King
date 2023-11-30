package uk.ac.aber.dcs.cs31620.fitnessking.model

import androidx.compose.ui.graphics.vector.ImageVector

data class WorkoutClass(
    val nameOfWorkout: String,
    val imageOfWorkout: ImageVector,
    val repetitionOfWorkouts: Int,
    val setsOfWorkout: Int,
    val weightsUsed: Int,
    val dropSet: Boolean
)
