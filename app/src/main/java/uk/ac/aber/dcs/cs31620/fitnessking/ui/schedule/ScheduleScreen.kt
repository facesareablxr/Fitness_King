package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
 * Top-level composable function for the Schedule Screen.
 * @param navController is the NavHostController for navigation
 * @param viewModel is the FitnessViewModel for accessing workout data
 */
@Composable
fun ScheduleScreenTopLevel(
    navController: NavHostController,
    viewModel: FitnessViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    // State variable for storing the list of workouts for the week
    var workoutsForWeek by remember {
        mutableStateOf<List<WorkoutWithExercises>>(emptyList())
    }

    // Fetching workouts for the week asynchronously
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
    ScheduleScreen(navController, workoutsForWeek, onDelete = { deletedWorkout ->
        // Remove the deleted workout from the list
        workoutsForWeek = workoutsForWeek.filter { it.dayOfWeek != deletedWorkout }
    })
}

/**
 * Represents the Schedule Screen content, displaying each workout scheduled for the week.
 * @param navController is the NavHostController for navigation
 * @param workoutsForWeek is the list of workouts with exercises for the week
 * @param onDelete is the callback for deleting a workout
 */
@Composable
fun ScheduleScreen(
    navController: NavHostController,
    workoutsForWeek: List<WorkoutWithExercises>,
    onDelete: (String) -> Unit
) {
    // Coroutine scope for handling asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    // Display the Top Level Scaffold with a floating action button
    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        floatingActionButton = {
            // Floating action button for adding a new workout
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
                ScheduleScreenContent(workoutsForWeek, onDelete)
            }
        }
    )
}

/**
 * Represents the Schedule Screen content, displaying each workout scheduled for the week.
 * @param workoutsForWeek is the list of workouts with exercises for the week
 * @param onDelete is the callback for deleting a workout
 */
@Composable
fun ScheduleScreenContent(
    workoutsForWeek: List<WorkoutWithExercises>,
    onDelete: (String) -> Unit
) {
    // List of days in order
    val dayOrder = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    // Sorting workouts based on the order of days
    val sortedWorkouts = workoutsForWeek.sortedBy { dayOrder.indexOf(it.dayOfWeek) }

    // LazyColumn for displaying workout cards
    LazyColumn {
        items(sortedWorkouts) { workout ->
            // Display each Workout Card
            WorkoutCard(workout, onDelete)
        }
    }
}

/**
 * Represents a stylized workout card that displays workout details.
 * @param workout is the WorkoutWithExercises object to display
 * @param onDelete is the callback for deleting a workout
 */
@Composable
fun WorkoutCard(
    workout: WorkoutWithExercises,
    onDelete: (String) -> Unit
) {
    // Check if there is at least one exercise in the workout
    workout.exercises.firstOrNull()?.let { firstExercise ->
        // Create a painter for the image
        val painter = rememberAsyncImagePainter(model = firstExercise.image)

        // Mutable state for handling dropdown menu visibility
        var isMenuVisible by remember { mutableStateOf(false) }

        // Card with rounded corners
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            // Column for organizing workout details
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Row for workout day and three-dot button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Workout day
                    Text(text = workout.dayOfWeek, fontWeight = FontWeight.Bold)

                    // Three-dot button
                    Box {
                        IconButton(
                            onClick = { isMenuVisible = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = isMenuVisible,
                            onDismissRequest = { isMenuVisible = false },
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(8.dp)
                        ) {
                            // Dropdown menu item for deleting the workout
                            DropdownMenuItem(
                                onClick = {
                                    isMenuVisible = false
                                    onDelete(workout.dayOfWeek)
                                }
                            ) {
                                Text("Delete Workout")
                            }
                        }
                    }
                }

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
                    // Display details for each exercise
                    Text(
                        text = "${exercise.name} - ${exercise.sets} sets | ${exercise.reps} reps | ${exercise.weight} kg"
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}