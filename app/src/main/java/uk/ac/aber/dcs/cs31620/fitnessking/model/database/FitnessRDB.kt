package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercisesDao

interface FitnessRDB {
    fun workoutDao(): WorkoutDao
    fun exerciseDao(): ExerciseDao
    fun workoutWithExercisesDao(): WorkoutWithExercisesDao
    fun closeDb()
}