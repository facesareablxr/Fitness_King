package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutDao

interface FitnessRDB {
    fun allDao():AllDao
    fun closeDb()
}