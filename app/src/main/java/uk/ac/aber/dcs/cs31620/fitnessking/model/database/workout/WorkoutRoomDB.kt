package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout // Adjust package as needed

import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutDao

interface WorkoutRoomDB{
    fun workoutDao(): WorkoutDao
    fun closeDB()
}