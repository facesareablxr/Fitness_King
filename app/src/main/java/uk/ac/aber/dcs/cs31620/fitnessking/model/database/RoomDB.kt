package uk.ac.aber.dcs.cs31620.fitnessking.model.database // Adjust package as needed

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.ExerciseDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.WorkoutDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.WorkoutEntity

interface RoomDB{
    fun ExerciseDao(): ExerciseDao
    fun WorkoutDao(): WorkoutDao
    fun closeDB()
}