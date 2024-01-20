package uk.ac.aber.dcs.cs31620.fitnessking.model.database

import android.content.Context

object Injection {
    fun getDatabase(context: Context): FitnessRDB =
        FitnessKingDB.getDatabase(context)

}