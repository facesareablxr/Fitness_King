package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.ExerciseDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.WorkoutDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.WorkoutEntity

@Database(entities = [ExerciseEntity::class, WorkoutEntity::class], version = 1)
abstract class FitnessDB : RoomDatabase(), RoomDB {

    abstract override fun ExerciseDao(): ExerciseDao
    abstract override fun WorkoutDao(): WorkoutDao

    override fun closeDB() {
        instance?.close()
        instance = null
    }

    companion object {
        private var instance: FitnessDB? = null

        fun getDatabase(context: Context): FitnessDB? {
            synchronized(this) {
                if (instance == null) {
                    instance =
                        Room.inMemoryDatabaseBuilder(
                            context.applicationContext,
                            FitnessDB::class.java
                        )
                            .build()
                }
                return instance!!
            }
        }
    }
}