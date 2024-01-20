package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise


import android.app.Application
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.Injection

class ExerciseRepository(application: Application) {
    private val exerciseDao = Injection.getDatabase(application).exerciseDao()

    fun insertExercise(exercise: ExerciseEntity): Long {
        return exerciseDao.insertExercise(exercise)
    }

    fun updateExercise(exercise: ExerciseEntity) {
        exerciseDao.updateExercise(exercise)
    }

    fun deleteExercise(exercise: ExerciseEntity) {
        exerciseDao.deleteExercise(exercise)
    }

    fun getAllExercises(): List<ExerciseEntity> {
        return exerciseDao.getAllExercises()
    }

    fun getExerciseById(exerciseId: Int): ExerciseEntity? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    fun toggleFavorite(exerciseId: Int, isFavorite: Boolean) {
        exerciseDao.toggleFavorite(exerciseId, isFavorite)
    }
}