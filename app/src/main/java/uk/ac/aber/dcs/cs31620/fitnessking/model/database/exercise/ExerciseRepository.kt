package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise


import android.app.Application
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection

class ExerciseRepository(application: Application) {
    private val exerciseDao = Injection.getDatabase(application).exerciseDao()
    // Exercise CRUD Functions
    fun getAllExercises() = exerciseDao.getAllExercises()

    fun insertExercise(exercise: ExerciseEntity) = exerciseDao.insertExercise(exercise)

    fun updateExercise(exercise: ExerciseEntity) = exerciseDao.updateExercise(exercise)

    fun deleteExercise(exercise: ExerciseEntity) = exerciseDao.deleteExercise(exercise)
}