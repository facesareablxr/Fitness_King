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
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.ExerciseIdsConverter
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutDao
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercises
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercisesCrossRef
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercisesDao

/**
 * Class to initialise the whole FitnessKing database with all the different tables of information
 */
@Database(
    entities = [WorkoutEntity::class, ExerciseEntity::class, WorkoutWithExercises::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExerciseIdsConverter::class)
abstract class FitnessKingDB : RoomDatabase(), FitnessRDB {
    abstract override fun workoutDao(): WorkoutDao
    abstract override fun exerciseDao(): ExerciseDao
    abstract override fun workoutWithExercisesDao(): WorkoutWithExercisesDao
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
                        .allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))
                        .build()
            }
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

        /**
         * Code to populate the database with some example data
         */
        private fun populateDatabase(context: Context, instance: FitnessKingDB) {
            val imagePath = "file:///android_asset/images/"


            // Exercises
            val exerciseDao = instance.exerciseDao()
            val exercises = listOf(
                ExerciseEntity(name = "Push-ups", sets = 3, reps = 10, weight = 0, isDropSet = false, image = "${imagePath}pushup.jpg", isFavourite = false),
                ExerciseEntity(name = "Squats", sets = 4, reps = 12, weight = 0, isDropSet = false, image = "${imagePath}squat.png", isFavourite = true),
                ExerciseEntity(name = "Pull-ups", sets = 3, reps = 8, weight = 0, isDropSet = true, image = "${imagePath}pullup.jpg", isFavourite = false),
                ExerciseEntity(name = "Bench press", sets = 3, reps = 6, weight = 60, isDropSet = true, image = "${imagePath}benchpress.jpg", isFavourite = false),
                ExerciseEntity(name = "Rows", sets = 3, reps = 10, weight = 50, isDropSet = false, image = "${imagePath}row.jpg", isFavourite = true)
            )

            for (exercise in exercises) {
                exerciseDao.insertExercise(exercise) //Adds to exercise table
            }

            // Workouts
            val workoutDao = instance.workoutDao()
            val workouts = listOf(
                WorkoutEntity(day = DaysOfWeek.Monday, focus = Focus.Chest, length = 45),
                WorkoutEntity(day = DaysOfWeek.Wednesday, focus = Focus.Legs, length = 60),
                WorkoutEntity(day = DaysOfWeek.Thursday, focus = Focus.Back, length = 50),
                WorkoutEntity(day = DaysOfWeek.Friday, focus = Focus.Full, length = 50)
            )
            for (workout in workouts) {
                workoutDao.insertWorkout(workout) //Adds to workout table
            }

            //WorkoutWithExercises
            val workoutExercisesDao = instance.workoutWithExercisesDao()

            val workoutWithExercises = listOf(
                WorkoutWithExercisesCrossRef(
                    workoutId = 1, // Monday workout
                    exerciseId = ExerciseIdsConverter().fromList(listOf(1, 4))
                ),
                WorkoutWithExercisesCrossRef(
                    workoutId = 2, // Wednesday workout
                    exerciseId = ExerciseIdsConverter().fromList(listOf(2, 5))
                ),
                WorkoutWithExercisesCrossRef(
                    workoutId = 3, // Thursday workout
                    exerciseId = ExerciseIdsConverter().fromList(listOf(3,2))
                ),
                WorkoutWithExercisesCrossRef(
                    workoutId = 4, //Friday workout
                    exerciseId = ExerciseIdsConverter().fromList(listOf(1,2,3,4))
                )
            )

            workoutExercisesDao.insertWorkoutWithExercisesCrossRef(workoutWithExercises) //Adds all information to the workoutwithexercises table
        }
    }
}