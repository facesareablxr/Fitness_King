package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise // Adjust package as needed

interface ExerciseRoomDB{
    fun exerciseDao(): ExerciseDao
    fun closeDB()
}