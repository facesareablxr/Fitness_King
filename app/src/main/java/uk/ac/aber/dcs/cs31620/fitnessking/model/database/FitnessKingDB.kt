package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus

/* This does not work, however, if it did, it would be a database class which would hold the
entities, and the cross reference class to allow for workouts to be assigned exercises
@Database(
    entities = [WorkoutEntity::class, ExerciseEntity::class, WorkoutWithExercisesMap::class],
    version = 1,
    exportSchema = false
)
abstract class FitnessKingDB : RoomDatabase(), FitnessRDB {

    abstract override fun allDao(): AllDao
    override fun closeDb() {
        instance?.close()
        instance = null
    }

    companion object {
        private var instance: FitnessKingDB? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): FitnessKingDB {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        FitnessKingDB::class.java,
                        "FitnessKingDB"
                    )
                        //.allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))
                        //.addMigrations(MIGRATION_1_2, MIGRATION_2_3
                        .build()
            } // if
            return instance as FitnessKingDB
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {
                        populateDatabase(context, getDatabase(context))
                    }
                }
            }
        }

        private fun populateDatabase(context: Context, instance: FitnessKingDB) {
            val allDao = instance.allDao()

            //Exercises

            val ex1 = ExerciseEntity(
                exerciseName = "Push-ups",
                sets = 3,
                reps = 10,
                weight = 0,
                isFavourite = false,
                isDropSet = true
            )
            val ex2 = ExerciseEntity(
                exerciseName = "Squats",
                sets = 4,
                reps = 12,
                weight = 0,
                isFavourite = true,
                isDropSet = false
            )
            val ex3 = ExerciseEntity(
                exerciseName = "Pull-ups",
                sets = 3,
                reps = 8,
                weight = 0,
                isFavourite = false,
                isDropSet = false
            )
            val ex4 = ExerciseEntity(
                exerciseName = "Bench press",
                sets = 3,
                reps = 6,
                weight = 60,
                isFavourite = false,
                isDropSet = false
            )
            val ex5 = ExerciseEntity(
                exerciseName = "Rows",
                sets = 3,
                reps = 10,
                weight = 50,
                isFavourite = true,
                isDropSet = true
            )

            allDao.insertExercise(ex1)
            allDao.insertExercise(ex2)
            allDao.insertExercise(ex3)
            allDao.insertExercise(ex4)
            allDao.insertExercise(ex5)


            // Workouts
            val wo1 = WorkoutEntity(
                workoutDay = DaysOfWeek.Monday,
                focus = Focus.Chest,
                length = 45,
                restTime = 10,
            )
            val wo2 = WorkoutEntity(
                workoutDay = DaysOfWeek.Wednesday,
                focus = Focus.Legs,
                length = 60,
                restTime = 12,
            )
            val wo3 = WorkoutEntity(
                workoutDay = DaysOfWeek.Thursday,
                focus = Focus.Back,
                length = 50,
                restTime = 14,
            )
            val wo4 = WorkoutEntity(
                workoutDay = DaysOfWeek.Sunday,
                focus = Focus.Full,
                length = 50,
                restTime = 22,
            )

            allDao.insertWorkout(wo1)
            allDao.insertWorkout(wo2)
            allDao.insertWorkout(wo3)
            allDao.insertWorkout(wo4)


        }
    }
}

 */