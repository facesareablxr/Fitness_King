package uk.ac.aber.dcs.cs31620.fitnessking.ui.workouts

import SavedWorkoutsTopAppBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Exercise
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.MainPageNavigationBar


/**
 * Data from the database, to be separated and put onto the cards
 */
val exerciseDatabase = listOf(
    Exercise("Exercise 1", 3, 12, 50, "Chest", false, "image_url_1"),
    Exercise("Exercise 2", 4, 10, 60, "Legs", true, "image_url_2"),
    Exercise("Exercise 3", 3, 15, 40, "Back", false, "image_url_3")
    // Add more exercises as needed
)

/**
 * Function to create the card details, with all the information put together using the later function
 * exerciseDetails. It is only a preview of all information held about one exercise.
 */
@Composable
fun ExerciseListItem(exercise: Exercise, onItemClick: (Exercise) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(exercise) },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = exercise.name)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Sets: ${exercise.sets}, Reps: ${exercise.reps}, Weight: ${exercise.weight}kg")
        }
    }
}

@Composable
fun ExerciseList(exercises: List<Exercise>, onItemClick: (Exercise) -> Unit) {
    LazyColumn {
        items(exercises) { exercise ->
            ExerciseListItem(exercise = exercise, onItemClick = onItemClick)
        }
    }
}

/**
 * Function to put together the card details
 */
@Composable
fun ExerciseDetails(exercise: Exercise) {
    Text(text = "Exercise Name: ${exercise.name}")
    Text(text = "Sets: ${exercise.sets}, Reps: ${exercise.reps}, Weight: ${exercise.weight}kg")
    Text(text = "Focus: ${exercise.focus}")
    Text(text = "Drop Set: ${if (exercise.dropSet) "Yes" else "No"}")
    Text(text = "Image URL: ${exercise.image}")
}

/**
 * Screen content put together with the top app bar
 */
@Composable
fun SavedWorkoutsScreen(navController: NavController) {
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    Column {
        SavedWorkoutsTopAppBar()
        if (selectedExercise == null) {
            ExerciseList(exercises = exerciseDatabase) { exercise ->
                selectedExercise = exercise
            }
        } else {
            ExerciseDetails(exercise = selectedExercise!!)
        }
    }
}
