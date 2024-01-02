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

@Database(entities = [ExerciseEntity::class, WorkoutEntity::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java,
                "fitnessking_db" // Use your database name here
            ).build()
        }
    }
}