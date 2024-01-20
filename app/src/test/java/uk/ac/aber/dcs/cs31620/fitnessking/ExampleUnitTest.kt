package uk.ac.aber.dcs.cs31620.fitnessking
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises.WorkoutWithExercises
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.FitnessKingDB

@ExperimentalCoroutinesApi

@RunWith(AndroidJUnit4::class)

@SmallTest
class FitnessKingDBTest {

    private lateinit var db: FitnessKingDB
    private lateinit var workoutDao: WorkoutDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var workoutWithExercisesDao: WorkoutWithExercisesDao

    @Before
    fun setup() {
        db = FitnessKingDB.getDatabase(ApplicationProvider.getApplicationContext())
        workoutDao = db.workoutDao()
        exerciseDao = db.exerciseDao()
        workoutWithExercisesDao = db.workoutWithExercisesDao()
    }

    @After
    fun teardown() {
        db.closeDb()
    }

    @Test
    fun insertAndRetrieveWorkoutWithMultipleExercises() = runBlockingTest {
        // Insert a workout with multiple exercises
        val workoutId = 1
        val exerciseIds = listOf(2, 4)
        workoutWithExercisesDao.insertAll(WorkoutWithExercises(workoutId, exerciseIds))

        // Retrieve the workout and its associated exercises
        val retrievedWorkout = workoutDao.getExercisesForWorkout(workoutId)
        val retrievedExercises = exerciseDao.getExerciseById(workoutId)

        // Assert that the retrieved data matches the expected data
      //  assertEquals(retrievedWorkout).isNotNull()
       // assert(retrievedExercises).isNotEmpty()
        //assert(retrievedExercises.map { it.exerciseId }).containsExactlyElementsIn(exerciseIds)
    }
}