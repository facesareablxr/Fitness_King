@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.WorkoutWithExercises
import uk.ac.aber.dcs.cs31620.fitnessking.model.datafiles.FitnessViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen

/**
 * Represents the Schedule Screen, displaying each workout scheduled for the week.
 * Allows users to select a card to view the workout in more detail.
 *
 * Author: Lauren Davis
 */

@Composable
fun ScheduleScreenTopLevel(
    navController: NavHostController,
    viewModel: FitnessViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()


    var workoutsForWeek by remember {
        mutableStateOf<List<WorkoutWithExercises>>(emptyList())
    }
    LaunchedEffect(key1 = Unit) {
        try {
            val result = viewModel.readWorkoutsWithExercises()
            coroutineScope.launch {
                workoutsForWeek = result
            }
        } catch (e: Exception) {
            // Handle the exception or log it
        }
    }

    // Display the Schedule Screen
    ScheduleScreen(navController, workoutsForWeek, viewModel)
}

/**
 * Represents the Schedule Screen content, displaying each workout scheduled for the week.
 *
 * @param navController: NavHostController used for navigation
 * @param workoutsForWeek: List of workouts with exercises
 * @param viewModel: FitnessViewModel for accessing workout data
 */
@Composable
fun ScheduleScreen(
    navController: NavHostController,
    workoutsForWeek: List<WorkoutWithExercises>,
    viewModel: FitnessViewModel
) {
    // Coroutine scope for asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    // Display the Top Level Scaffold with a floating action button
    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddWorkout.route)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add workout")
            }
        },
        pageContent = { innerPadding ->
            // Surface for the page content
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Display the Schedule Screen Content
                ScheduleScreenContent(workoutsForWeek, viewModel) {
                    // Handle workout click (if needed)
                }
            }
        }
    )
}

/**
 * Represents the Schedule Screen content, displaying each workout scheduled for the week.
 *
 * @param workoutsForWeek: List of workouts with exercises
 * @param viewModel: FitnessViewModel for accessing workout data
 * @param onWorkoutClick: Callback for handling workout click events
 */
@Composable
fun ScheduleScreenContent(
    workoutsForWeek: List<WorkoutWithExercises>,
    viewModel: FitnessViewModel,
    onWorkoutClick: () -> Unit
) {
    // LazyColumn for displaying workout cards
    LazyColumn {
        items(workoutsForWeek) { workout ->
            // Display each Workout Card
            WorkoutCard(workout, onWorkoutClick)
        }
    }
}


/**
 * Represents a stylized workout card that displays workout details.
 *
 * @param workout: WorkoutWithExercises object
 * @param onWorkoutClick: Callback for handling workout click events
 */
@Composable
fun WorkoutCard(workout: WorkoutWithExercises, onWorkoutClick: () -> Unit) {
    // Check if there is at least one exercise in the workout
    workout.exercises.firstOrNull()?.let { firstExercise ->
        // Create a painter for the image
        val painter = rememberAsyncImagePainter(model = firstExercise.image)

        // Card with rounded corners
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = onWorkoutClick
        ) {
            // Column for organizing workout details
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Workout day
                Text(text = workout.dayOfWeek, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                // Workout focus
                Text(text = "Focus: ${workout.focus}")
                Spacer(modifier = Modifier.height(16.dp))

                // Display the image
                Image(
                    painter = painter,
                    contentDescription = "Exercise Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp)  // Adjust the height as needed
                        .align(Alignment.CenterHorizontally),  // Center the image horizontally
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Text details for each exercise
                Text(text = "Exercises:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                workout.exercises.forEach { exercise ->
                    Text(
                        text = "${exercise.name} - ${exercise.sets} sets | ${exercise.reps} reps | ${exercise.weight} kg"
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}