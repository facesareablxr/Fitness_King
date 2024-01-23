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
import java.time.LocalDate

/**
 * This is the viewmodel class which reads, writes and updates files.
 */
class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private var exerciseFile: File = File(application.filesDir, "exercises.csv")
    private var workoutFile: File = File(application.filesDir, "workouts.csv")
    private var workoutWithExerciseFile: File =
        File(application.filesDir, "workoutswithexercises.txt")

    /** Manages the reading of the exercises from exercises.csv
     * This one is different to the rest due to the errors I was having with trying to read from
     * text file. Sorry!
     * @return exercises in a list
     */
    fun readExercises(): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        exerciseFile.readLines().drop(1).mapNotNull { line ->
            try {
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
                null
            }
        }
            .forEach { exercises.add(it) }
        return exercises
    }

    /**
     * Manages the addition of exercises by ensuring the new exercise is in the same format
     */
    fun addExercise(
        name: String,
        sets: Int,
        reps: Int,
        weight: Int,
        dropset: Boolean,
        lengthOfExercise: Int,
        image: String,
        favourite: Boolean,
        rest: Int,
        focus: String
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
            val currentExercises = readExercises().toMutableList()
            currentExercises.add(newExercise)
            updateExercisesFile(currentExercises)
        } catch (e: Exception) {
            println("Error adding exercise")
        }
    }

    fun deleteExercise(exerciseName: String) {
        try {
            val exercises = readExercises().toMutableList()
            val index = exercises.indexOfFirst { it.name == exerciseName }

            if (index != -1) {
                exercises.removeAt(index)
                updateExercisesFile(exercises)
            } else {
                throw NoSuchElementException("Exercise not found")
            }
        } catch (e: Exception) {
            println("Error deleting exercise")
        }
    }

    fun updateExercisesFile(exercises: List<Exercise>) {
        try {
            exerciseFile.writeText("")

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

    fun deleteWorkoutWithExercises(workoutDay: String?) {
        try {
            val workoutsWithExercises = readWorkoutsWithExercises().toMutableList()
            val index = workoutsWithExercises.indexOfFirst { it.dayOfWeek == workoutDay }

            if (index != -1) {
                workoutsWithExercises.removeAt(index)
                updateWorkoutsWithExercisesFile(workoutsWithExercises)
            } else {
                throw NoSuchElementException("Workout not found")
            }
        } catch (e: Exception) {
            println("Error deleting workout")
        }
    }

    private fun updateWorkoutsWithExercisesFile(workoutsWithExercises: List<WorkoutWithExercises>) {
        try {
            workoutWithExerciseFile.writeText("")

            val csvContent = buildString {
                appendLine("""day,focus,name,sets,reps,weight,isDropSet,length,image,isFavourite,restTime""")
                workoutsWithExercises.forEach { workoutWithExercises ->
                    val workoutLine = buildString {
                        append("${workoutWithExercises.dayOfWeek},${workoutWithExercises.focus}")
                    }
                    workoutWithExercises.exercises.forEach { exercise ->
                        appendLine("$workoutLine," +
                                "${exercise.name},${exercise.sets},${exercise.reps}," +
                                "${exercise.weight},${exercise.isDropSet},${exercise.length}," +
                                "${exercise.image},${exercise.isFavourite},${exercise.restTime}")
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

    fun addWorkoutWithExercises(workoutWithExercises: WorkoutWithExercises) {
        try {
            val workoutsWithExercises = readWorkoutsWithExercises().toMutableList()
            workoutsWithExercises.add(workoutWithExercises)
            writeWorkoutsWithExercises(workoutsWithExercises)
        } catch (e: Exception) {
            println("Error adding workout with exercises")
        }
    }

    fun getAllWorkoutsWithExercises(): List<WorkoutWithExercises> {
        return try {
            readWorkoutsWithExercises()
        } catch (e: Exception) {
            emptyList()
        }
    }



}