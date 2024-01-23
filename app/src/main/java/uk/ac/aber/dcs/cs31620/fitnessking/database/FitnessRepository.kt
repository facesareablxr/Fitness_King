package uk.ac.aber.dcs.cs31620.fitnessking.database

/*
This is the repository class, this would manage the data between the DAO and the view model.
This does not work.

class FitnessRepository(application: Application) {
    private val allDao = Injection.getDatabase(application).allDao()

    // Exercises
    fun insertExercise(exercise: ExerciseEntity) {
        allDao.insertExercise(exercise)
    }

    fun updateExercise(exercise: ExerciseEntity) {
        allDao.updateExercise(exercise)
    }

    fun deleteExercise(exercise: ExerciseEntity) {
        allDao.deleteExercise(exercise)
    }

    val allExercises: LiveData<List<ExerciseEntity>> = allDao.getAllExercises()


    fun insertWorkout(workout: WorkoutEntity){
        allDao.insertWorkout(workout)
    }

    fun updateWorkout(workout: WorkoutEntity){
        allDao.updateWorkout(workout)
    }

    fun deleteWorkout(workout: WorkoutEntity){
        allDao.deleteWorkout(workout)
    }

    val allWorkouts: LiveData<List<WorkoutEntity>> = allDao.getAllWorkouts()

    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutEntity>> {
        return allDao.getAllWorkoutsWithExercises()
    }

    fun getWorkoutsForDay(day: DayOfWeek): List<WorkoutEntity> {
        return allDao.getWorkoutsForDay(day)
    }

    fun getTodaysWorkoutWithExercises(day: String): LiveData<WorkoutWithExercises> {
        return allDao.getWorkoutWithExercisesForDate(day)
    }
}

 */

