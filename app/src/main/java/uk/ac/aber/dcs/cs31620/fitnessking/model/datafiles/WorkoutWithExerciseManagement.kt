package uk.ac.aber.dcs.cs31620.fitnessking.model.datafiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Exercise
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Workout
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.WorkoutWithExercises
import java.io.File
import java.io.IOException
import java.time.LocalDate

/**
 * ViewModel class responsible for managing fitness-related data, including reading, writing, and updating files.
 */
class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    // Files for storing exercise, workout, and workout with exercises data, will create them if it cannot find them
    private var exerciseFile: File = createFile(application, "exercises.csv")
    private var workoutFile: File = createFile(application, "workouts.csv")
    private var workoutWithExerciseFile: File = createFile(application, "workoutswithexercises.txt")

    /**
     * Will create the file if it does not already exist.
     * @return file at the desired location
     */
    private fun createFile(application: Application, fileName: String): File {
        val file = File(application.filesDir, fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                println("Error creating file")
            }
        }
        return file
    }

    /**
     * Reads exercises from the exercises.csv file, different to workoutwithexercises.txt due to issues
     * reading and writing, I would have liked for it to be consistent but ran out of time! Sorry.
     * @return List of Exercise objects
     */
    fun readExercises(): List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        // Read the exercises from the file
        exerciseFile.readLines().drop(1).mapNotNull { line ->
            try {
                // Parse CSV line and create exercise object
                val values = CsvReader().readAll(line).flatten()
                Exercise(
                    name = values[0],
                    sets = values[1].toInt(),
                    reps = values[2].toInt(),
                    weight = values[3].toInt(),
                    isDropSet = values[4].toBoolean(),
                    length = values[5].toInt(),
                    image = values[6],
                    isFavourite = values[7].toBoolean(),
                    restTime = values[8].toInt(),
                    focus = values[9]
                )
            } catch (e: Exception) {
                // Return null as it would skip over the lines
                null
            }
        }
            .forEach { exercises.add(it) }
        return exercises // Return the exercises to the application
    }

    /**
     * Adds a new exercise to the exercises list and updates the file.
     */
    fun addExercise(
        // Create new Exercise object
        name: String,
        sets: Int,
        reps: Int,
        weight: Int,
        dropset: Boolean,
        lengthOfExercise: Int,
        image: String,
        favourite: Boolean,
        rest: Int,
        focus: String,
    ) {
        try {
            val newExercise = Exercise(
                name = name,
                sets = sets,
                reps = reps,
                weight = weight,
                isDropSet = dropset,
                length = lengthOfExercise,
                image = image,
                isFavourite = favourite,
                restTime = rest,
                focus = focus
            )
            // Read current exercises, add new exercise, and update file
            val currentExercises = readExercises().toMutableList()
            currentExercises.add(newExercise)
            updateExercisesFile(currentExercises)
        } catch (e: Exception) {
            println("Error adding exercise")
        }
    }

    /**
     * Updates the exercises.csv file with the provided list of exercises.
     */
    fun updateExercisesFile(exercises: List<Exercise>) {
        try {
            // Clear files existing content, essentially delete it all
            exerciseFile.writeText("")

            // Write new CSV header to the file
            val csvContent = buildString {
                appendLine("""name","sets","reps","weight","isDropSet","length","image","isFavourite","restTime","focus""")
                exercises.forEach { exercise ->
                    appendLine("${exercise.name},${exercise.sets},${exercise.reps},${exercise.weight},${exercise.isDropSet},${exercise.length},${exercise.image},${exercise.isFavourite},${exercise.restTime},${exercise.focus}")
                }
            }
            exerciseFile.appendText(csvContent)
        } catch (e: Exception) {
            println("Error updating exercises file: ${e.message}")
        }
    }

    private fun readWorkouts(): List<Workout> {
        return try {
            val file = workoutFile
            val lines = file.readLines().drop(1) // Skip header
            lines.map {
                val tokens = it.split(",")
                Workout(tokens[0].replace("\"", ""), tokens[1].replace("\"", ""))
            }
        } catch (e: Exception) {
            println("Error reading exercises from file")
            emptyList()
        }
    }

    fun readWorkoutsWithExercises(): List<WorkoutWithExercises> {
        return try {
            val file = workoutWithExerciseFile
            val lines = file.readLines().drop(1) // Skip header
            lines.map { it ->
                val tokens = it.split(",")
                val exercises = tokens.subList(2, tokens.size).chunked(10)
                    .map {
                        Exercise(
                            it[0].replace("\"", ""),
                            it[1].toInt(),
                            it[2].toInt(),
                            it[3].toInt(),
                            it[4].toBoolean(),
                            it[5].toInt(),
                            it[6].replace("\"", ""),
                            it[7].toBoolean(),
                            it[8].toInt(),
                            it[9].replace("\"", "")
                        )
                    }
                WorkoutWithExercises(
                    tokens[0].replace("\"", ""),
                    tokens[1].replace("\"", ""),
                    exercises
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun writeWorkoutsWithExercises(workoutsWithExercises: List<WorkoutWithExercises>) {
        try {
            val file = workoutWithExerciseFile
            val header = "\"Workout Day\",\"Workout Focus\",\"Exercises\""
            val lines = mutableListOf(header)
            lines.addAll(workoutsWithExercises.map {
                val exerciseData = it.exercises.joinToString(",") { exercise ->
                    "\"${exercise.name}\",${exercise.sets},${exercise.reps},${exercise.weight}," +
                            "${exercise.isDropSet},${exercise.length},\"${exercise.image}\"," +
                            "${exercise.isFavourite},${exercise.restTime},\"${exercise.focus}\""
                }
                "\"${it.dayOfWeek}\",\"${it.focus}\",${exerciseData}"
            })
            file.writeText(lines.joinToString("\n"))
        } catch (_: Exception) {
            println("Error writing to workout with exercises")
        }
    }

    fun addWorkoutWithExercises(workoutWithExercises: WorkoutWithExercises) {
        try {
            val workoutsWithExercises = readWorkoutsWithExercises().toMutableList()
            workoutsWithExercises.add(workoutWithExercises)
            writeWorkoutsWithExercises(workoutsWithExercises)
        } catch (e: Exception) {
            println("Error adding workout with exercises")
        }
    }

    fun updateWorkoutsWithExercisesFile(workoutsWithExercises: List<WorkoutWithExercises>) {
        try {
            workoutWithExerciseFile.writeText("")

            val csvContent = buildString {
                appendLine("""day,focus,name,sets,reps,weight,isDropSet,length,image,isFavourite,restTime""")
                workoutsWithExercises.forEach { workoutWithExercises ->
                    val workoutLine = buildString {
                        append("${workoutWithExercises.dayOfWeek},${workoutWithExercises.focus}")
                    }
                    workoutWithExercises.exercises.forEach { exercise ->
                        appendLine(
                            "$workoutLine," +
                                    "${exercise.name},${exercise.sets},${exercise.reps}," +
                                    "${exercise.weight},${exercise.isDropSet},${exercise.length}," +
                                    "${exercise.image},${exercise.isFavourite},${exercise.restTime}"
                        )
                    }
                }
            }
            workoutWithExerciseFile.appendText(csvContent)
        } catch (e: Exception) {
            println("Error updating workouts with exercises file")
        }
    }


    fun getAvailableWorkoutDays(): LiveData<List<String>> = liveData {
        try {
            val scheduledDays = withContext(Dispatchers.IO) {
                readWorkouts().map { it.day }
            }
            val allDays =
                getApplication<Application>().resources.getStringArray(R.array.DayOfWeek).toList()
            val availableDays = allDays - scheduledDays.toSet()

            // Filter out "any day" if present
            val filteredDays = availableDays.filter { it != "any day" }

            // Emit the filtered available days
            emit(filteredDays)
        } catch (e: Exception) {
            println("Error getting available workout days")
        }
    }

    fun getTodaysWorkout(currentDate: LocalDate): List<WorkoutWithExercises> {
        val workouts = readWorkoutsWithExercises()
        val todaysWorkoutsWithExercise = workouts.filter {
            it.dayOfWeek.equals(
                currentDate.dayOfWeek.toString(),
                ignoreCase = true
            )
        }
        return todaysWorkoutsWithExercise
    }

    fun getTodaysExercisesWithDetails(currentDate: LocalDate): List<Exercise>? {
        val workouts = readWorkoutsWithExercises()
        val todaysWorkout = workouts.find {
            it.dayOfWeek.equals(
                currentDate.dayOfWeek.toString(),
                ignoreCase = true
            )
        }

        return todaysWorkout?.exercises
    }

    fun getTodaysWorkoutWithDetails(currentDate: LocalDate): Pair<String?, String?> {
        val workouts = readWorkoutsWithExercises()
        val todaysWorkout = workouts.find {
            it.dayOfWeek.equals(
                currentDate.dayOfWeek.toString(),
                ignoreCase = true
            )
        }

        return Pair(todaysWorkout?.dayOfWeek, todaysWorkout?.focus)
    }

    fun calculateWorkoutLength(selectedExercises: List<Exercise>, restTime: Int): String {
        // Calculate the total workout length based on the rest time and exercise lengths
        val totalExerciseLength = selectedExercises.sumOf { it.length }
        val totalRestTime = (selectedExercises.size - 1) * restTime // Rest time between exercises
        val totalLength = totalExerciseLength + totalRestTime

        // Format the total length (you can customize the formatting based on your needs)
        val hours = totalLength / 60
        val minutes = totalLength % 60

        return "$hours hours $minutes minutes"
    }

    fun getAllWorkoutsWithExercises(): List<WorkoutWithExercises> {
        return try {
            readWorkoutsWithExercises()
        } catch (e: Exception) {
            emptyList()
        }
    }
}