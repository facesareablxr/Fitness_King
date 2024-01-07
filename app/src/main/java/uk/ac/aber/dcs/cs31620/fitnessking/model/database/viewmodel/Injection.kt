package uk.ac.aber.dcs.cs31620.fitnessking.model.database.viewmodel

import android.content.Context
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.FitnessDB
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.RoomDB

object Injection {
    fun getDatabase(context: Context): RoomDB =
        FitnessDB.getDatabase(context)!!
}