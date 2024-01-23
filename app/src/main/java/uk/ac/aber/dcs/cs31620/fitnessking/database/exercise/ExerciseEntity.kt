package uk.ac.aber.dcs.cs31620.fitnessking.database.exercise


/*
 * ExerciseViewModel manages the exercises in the database, uses the Dao class
 * This does not work.

/**
 * This is the entity class for the exercises, it defines the different columns within the exercise
 * table
 * @author Lauren Davis
 */
@Entity(
    tableName = "exercise_table"
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "exercise_name")
    val exerciseName: String = "",
    val sets: Int = 0,
    val reps: Int = 0,
    val weight: Int = 0,
    val isDropSet: Boolean = false,
    val image: String = "",
    val isFavourite: Boolean = false
)

 */