package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.TypeConverter


class ExerciseIdsConverter {
    @TypeConverter
    fun fromList(exerciseId: List<Int>): String {
        return exerciseId.joinToString(",")
    }

    @TypeConverter
    fun toList(exerciseIdString: String): List<Int> {
        return exerciseIdString.split(",").map { it.toInt() }
    }
}