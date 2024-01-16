package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkoutEntity::class], version = 1)
abstract class WorkoutDB : RoomDatabase(), WorkoutRoomDB {

    abstract override fun workoutDao(): WorkoutDao

    override fun closeDB() {
        instance?.close()
        instance = null
    }

    companion object {
        private var instance: WorkoutDB? = null

        fun getWorkoutDatabase(context: Context): WorkoutDB? {
            synchronized(this) {
                if (instance == null) {
                    instance =
                        Room.inMemoryDatabaseBuilder(
                            context.applicationContext,
                            WorkoutDB::class.java
                        )
                            .build()
                }
                return instance!!
            }
        }
    }
}