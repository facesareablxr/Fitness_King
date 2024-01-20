package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise


import android.app.Application
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection

class ExerciseRepository(application: Application) {
    private val allDao = Injection.getDatabase(application).allDao()
    // Exercise CRUD Functions
    fun getAllExercises() = allDao.getAllExercises()

    fun insertExercise(exercise: ExerciseEntity) = allDao.insertExercise(exercise)

    fun updateExercise(exercise: ExerciseEntity) = allDao.updateExercise(exercise)

    fun deleteExercise(exercise: ExerciseEntity) = allDao.deleteExercise(exercise)

    fun getExerciseById(exerciseId: Int): ExerciseEntity {
        return allDao.getExerciseById(exerciseId.toLong())
    }
}