package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseEntity::class], version = 1)
abstract class ExerciseDB : RoomDatabase(), ExerciseRoomDB {
    abstract override fun exerciseDao(): ExerciseDao

    override fun closeDB() {
        instance?.close()
        instance = null
    }

    companion object {
        private var instance: ExerciseDB? = null

        fun getExerciseDatabase(context: Context): ExerciseDB? {
            synchronized(this) {
                if (instance == null) {
                    instance =
                        Room.inMemoryDatabaseBuilder(
                            context.applicationContext,
                            ExerciseDB::class.java
                        )
                            .build()
                }
                return instance!!
            }
        }
    }
}