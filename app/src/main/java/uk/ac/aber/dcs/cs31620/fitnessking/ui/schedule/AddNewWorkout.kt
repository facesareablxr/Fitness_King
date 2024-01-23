package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Exercise
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.WorkoutWithExercises
import uk.ac.aber.dcs.cs31620.fitnessking.model.datafiles.FitnessViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.ButtonSpinner
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.SmallTopAppBar

/**
 * Top-level composable function for the Add New Workout screen.
 * @param navController is the NavHostController for navigation
 */
@Composable
fun AddNewWorkoutTopLevel(navController: NavHostController) {
    AddNewWorkout(navController)
}

/**
 * Main setup for the Add New Workout screen, initializing variables and setting up the scaffold for content.
 * Uses SmallTopAppBar for the top bar customization.
 * @param navController is the NavHostController for navigation
 */
@Composable
fun AddNewWorkout(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

    val fitnessViewModel: FitnessViewModel = viewModel()

    // Fetching exercises from the view model
    var exercises by remember {
        mutableStateOf<List<Exercise>>(emptyList())
    }

    // Launched effect to read exercises asynchronously
    LaunchedEffect(key1 = Unit) {
        try {
            val result = fitnessViewModel.readExercises()
            coroutineScope.launch {
                exercises = result
            }
        } catch (e: Exception) {
            println("Cannot read exercises")
        }
    }

    // Append the focus values to an array
    val values = stringArrayResource(id = R.array.Focus)
    val focusValues = values.copyOfRange(1, values.size)

    // Get the list of workout days that are not already scheduled
    val availableDays by remember { fitnessViewModel.getAvailableWorkoutDays() }.observeAsState(
        emptyList()
    )

    // State variables for selected exercises and workout details
    var selectedExercises by remember { mutableStateOf(emptyList<Exercise>()) }
    var dayOfWorkout by remember { mutableStateOf(availableDays.firstOrNull() ?: "") }
    var exerciseFocus by remember { mutableStateOf("") }
    var workoutLength by remember { mutableStateOf("") }

    // Scaffold for the screen layout
    Scaffold(
        topBar = { SmallTopAppBar(navController, title = "Add Workout") },
        floatingActionButton = {
            // Floating action button to confirm the workout addition
            FloatingActionButton(
                onClick = {
                    if (dayOfWorkout.isNotEmpty() && exerciseFocus.isNotEmpty() && selectedExercises.isNotEmpty()) {
                        // Creating WorkoutWithExercises and adding it to the view model
                        val workoutWithExercises = WorkoutWithExercises(
                            dayOfWeek = dayOfWorkout,
                            focus = exerciseFocus,
                            exercises = selectedExercises
                        )
                        fitnessViewModel.addWorkoutWithExercises(workoutWithExercises)

                        // Writing updated workouts to storage
                        val workoutsWithExercises = fitnessViewModel.getAllWorkoutsWithExercises()
                        fitnessViewModel.writeWorkoutsWithExercises(workoutsWithExercises)

                        // Navigating back
                        navController.navigateUp()
                    } else {
                        // Displaying a snackbar if required fields are not filled
                        coroutineScope.launch {
                            SnackbarHostState().showSnackbar(
                                message = "Please fill in all required fields",
                                actionLabel = "Dismiss",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            ) {
                // Checkmark icon for the floating action button
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.addExercise)
                )
            }
        },
        content = { innerPadding ->
            // Main content of the screen
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Display day of the week input
                Text(text = "Day of the week:")
                DayOfWeekInput(
                    values = availableDays.toTypedArray(),
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentWidth(),
                    updateDayOfWeek = {
                        dayOfWorkout = it
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display focus input
                Text(text = "Focus:")
                FocusInput(
                    values = focusValues,
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentWidth(),
                    updateFocus = {
                        exerciseFocus = it
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display exercise selection button
                var isDialogVisible by remember { mutableStateOf(false) }
                Button(
                    onClick = { isDialogVisible = true },
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(12.dp)
                ) {
                    Text("Select Exercises")
                }

                // Display selected exercises
                Text("Selected Exercises:")
                selectedExercises.forEach { exercise ->
                    Text(text = exercise.name)
                }

                // Exercise selection dialog
                if (isDialogVisible) {
                    ExerciseSelectionAlertDialog(
                        onDismissRequest = {
                            isDialogVisible = false
                        },
                        viewModel = fitnessViewModel,
                        onExerciseSelected = {
                            selectedExercises = it
                            isDialogVisible = false

                            // Calculate workout length
                            workoutLength =
                                fitnessViewModel.calculateWorkoutLength(selectedExercises, 0)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Display workout length
                WorkoutLength(workoutLength)
            }
        }
    )
}

/**
 * Function to add workout only on days that are not already scheduled.
 * @param values is the array of available days
 * @param modifier is the modifier for the DayOfWeekInput
 * @param updateDayOfWeek is the callback to update the selected day of the week
 */
@Composable
fun DayOfWeekInput(
    values: Array<String>,
    modifier: Modifier,
    updateDayOfWeek: (String) -> Unit
) {
    ButtonSpinner(
        items = values.asList(),
        modifier = modifier,
        itemClick = {
            updateDayOfWeek(it)
        },
        label = "Day of Week"
    )
}

/**
 * Function to display focus input based on available exercises.
 * @param values is the array of available focus values
 * @param modifier is the modifier for the FocusInput
 * @param updateFocus is the callback to update the selected focus
 */
@Composable
fun FocusInput(
    values: Array<String>,
    modifier: Modifier,
    updateFocus: (String) -> Unit
) {
    ButtonSpinner(
        items = values.asList(),
        modifier = modifier,
        itemClick = {
            updateFocus(it)
        },
        label = "Focus"
    )
}

/**
 * Exercise selection dialog for choosing exercises to add to the workout.
 * @param onDismissRequest is the callback for dismissing the dialog
 * @param dialogTitle is the title of the dialog
 * @param onExerciseSelected is the callback for handling selected exercises
 * @param viewModel is the FitnessViewModel for accessing exercise data
 */
@Composable
fun ExerciseSelectionAlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String = "Select Exercises",
    onExerciseSelected: (List<Exercise>) -> Unit,
    viewModel: FitnessViewModel = viewModel()
) {
    // Fetching exercises from the view model
    val exercises = viewModel.readExercises()
    var selectedExercises by remember { mutableStateOf(emptyList<Exercise>()) }

    // AlertDialog for exercise selection
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = {
            ExerciseSelectionDialogContent(
                exercises = exercises,
                selectedExercises = selectedExercises,
                onSelectionChanged = { selectedExercises = it }
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            // Confirm button to save selected exercises
            TextButton(onClick = { onExerciseSelected(selectedExercises) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog
            TextButton(onClick = onDismissRequest) {
                Text("Dismiss")
            }
        }
    )
}

/**
 * Content of the exercise selection dialog, displaying a list of exercises with checkboxes.
 * @param exercises is the list of available exercises
 * @param selectedExercises is the list of currently selected exercises
 * @param onSelectionChanged is the callback for handling changes in the selection
 */
@Composable
private fun ExerciseSelectionDialogContent(
    exercises: List<Exercise>,
    selectedExercises: List<Exercise>,
    onSelectionChanged: (List<Exercise>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Select Exercises")

        // LazyColumn displaying a list of exercises with checkboxes
        LazyColumn {
            items(exercises) { exercise ->
                ExerciseSelectionItem(exercise, selectedExercises, onSelectionChanged)
            }
        }
    }
}

/**
 * Item in the exercise selection dialog, representing a single exercise with a checkbox.
 * @param exercise is the exercise to display
 * @param selectedExercises is the list of currently selected exercises
 * @param onSelectionChanged is the callback for handling changes in the selection
 */
@Composable
private fun ExerciseSelectionItem(
    exercise: Exercise,
    selectedExercises: List<Exercise>,
    onSelectionChanged: (List<Exercise>) -> Unit
) {
    val isChecked = selectedExercises.contains(exercise)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelectionChanged(
                    if (isChecked) {
                        selectedExercises - exercise
                    } else {
                        selectedExercises + exercise
                    }
                )
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox for exercise selection
        Checkbox(
            checked = isChecked,
            onCheckedChange = null
        )
        Text(
            text = exercise.name,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

/**
 * Display for the total workout length in a card.
 * @param totalLength is the total length of the workout
 */
@Composable
fun WorkoutLength(totalLength: String) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(id = R.string.totalTime))
            Text(text = totalLength)
        }
    }
}
