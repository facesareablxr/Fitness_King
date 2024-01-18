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
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseIdsConverter
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercises
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus


@Database(entities = [WorkoutEntity::class, ExerciseEntity::class, WorkoutWithExercises::class], version = 1, exportSchema = false)
@TypeConverters(ExerciseIdsConverter::class)
abstract class FitnessKingDB : RoomDatabase(), FitnessRDB {

    abstract override fun workoutDao(): WorkoutDao
    abstract override fun exerciseDao(): ExerciseDao
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
                    Room.databaseBuilder<FitnessKingDB>(
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
                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        private fun populateDatabase(context: Context, instance: FitnessKingDB) {

            // Exercises
            val exerciseDao = instance.exerciseDao()
            val exercises = listOf(
                ExerciseEntity(name = "Push-ups", sets = 3, reps = 10, weight = 0),
                ExerciseEntity(name = "Squats", sets = 4, reps = 12, weight = 0),
                ExerciseEntity(name = "Pull-ups", sets = 3, reps = 8, weight = 0),
                ExerciseEntity(name = "Bench press", sets = 3, reps = 6, weight = 60),
                ExerciseEntity(name = "Rows", sets = 3, reps = 10, weight = 50)
            )
            for (exercise in exercises) {
                exerciseDao.insertExercise(exercise)
            }

            // Workouts
            val workoutDao = instance.workoutDao()
            val workoutIds = mutableListOf<Long>()

            val workouts = listOf(
                WorkoutEntity(day = DaysOfWeek.Monday, focus = Focus.Chest, length = 45),
                WorkoutEntity(day = DaysOfWeek.Wednesday, focus = Focus.Legs, length = 60),
                WorkoutEntity(day = DaysOfWeek.Thursday, focus = Focus.Back, length = 50),
                WorkoutEntity(day = DaysOfWeek.Friday, focus = Focus.Full, length = 50)
            )

            for (workout in workouts) {
                workoutDao.insertWorkout(workout)
            }

            // Associate exercises with workouts (maintain exercise order)
            /*for (i in 0 until workouts.size) {
                val workoutId = workoutIds[i]
                val exerciseIds = when (i) {
                    0 -> listOf(1, 4) // Push-ups, bench press for Chest workout
                    1 -> listOf(2, 5) // Squats, rows for Legs workout
                    else -> listOf(3) // Pull-ups for Back workout
                }

                for (exerciseId in exerciseIds) {
                    val workoutWithExercise = WorkoutWithExercises(workoutId, exerciseId)
                    instance.workoutWith().insert(workoutWithExercise)
                }
            }*/
        }
    }
}
