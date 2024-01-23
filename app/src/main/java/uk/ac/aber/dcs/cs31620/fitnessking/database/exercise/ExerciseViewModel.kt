package uk.ac.aber.dcs.cs31620.fitnessking.database.exercise

/*
 * ExerciseViewModel manages the exercises in the database, uses the Dao class
 * This does not work.

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FitnessRepository = FitnessRepository(application)

    val allExercises: LiveData<List<ExerciseEntity>> = repository.allExercises

    fun insertExercise(exercise: ExerciseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExercise(exercise)
        }
    }

    // Function to update an existing exercise
    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateExercise(exercise)
        }
    }

    // Function to delete an exercise
    fun deleteExercise(exercise: ExerciseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteExercise(exercise)
        }
    }

}

 */