package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import android.content.Context
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseDB
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseRoomDB
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutDB
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutRoomDB

object Injection {
    fun getWorkoutDatabase(context: Context): WorkoutRoomDB =
        WorkoutDB.getWorkoutDatabase(context)!!

    fun getExerciseDatabase(context: Context): ExerciseRoomDB =
        ExerciseDB.getExerciseDatabase(context)!!
}