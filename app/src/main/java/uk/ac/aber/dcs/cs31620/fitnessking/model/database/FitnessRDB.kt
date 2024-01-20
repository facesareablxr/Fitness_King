package uk.ac.aber.dcs.cs31620.fitnessking.model.database

interface FitnessRDB {
    fun allDao():AllDao
    fun closeDb()
}