package uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise

import androidx.room.TypeConverter


class ExerciseIdsConverter {
    @TypeConverter
    fun fromList(exerciseIds: List<Int>): String {
        return exerciseIds.joinToString(",")
    }
    @TypeConverter
    fun toList(exerciseIdsString: String): List<Int> {
        return exerciseIdsString.split(",").map { it.toInt() }
    }
}