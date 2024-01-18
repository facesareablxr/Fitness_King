package uk.ac.aber.dcs.cs31620.fitnessking.model.database.workoutswithexercises

import androidx.room.TypeConverter


class ExerciseIdsConverter {
    @TypeConverter
    fun fromList(exerciseId: List<Long>): String {
        return exerciseId.joinToString(",")
    }

    @TypeConverter
    fun toList(exerciseIdString: String): List<Long> {
        return exerciseIdString.split(",").map { it.toLong() }
    }
}