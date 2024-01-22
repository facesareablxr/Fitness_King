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


@Composable
fun AddNewWorkoutTopLevel(navController: NavHostController) {
    AddNewWorkout(navController)
}

@Composable
fun AddNewWorkout(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

    val fitnessViewModel: FitnessViewModel = viewModel()

    var exercises by remember {
        mutableStateOf<List<Exercise>>(emptyList())
    }

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

    val values = stringArrayResource(id = R.array.Focus)
    val focusValues = values.copyOfRange(1, values.size)

    // Get the list of workout days that are not already scheduled
    val availableDays by remember { fitnessViewModel.getAvailableWorkoutDays() }.observeAsState(emptyList())

    // State for holding selected exercises
    var selectedExercises by remember { mutableStateOf(emptyList<Exercise>()) }

    // State for workout details
    var dayOfWorkout by remember { mutableStateOf(availableDays.firstOrNull() ?: "") }
    var exerciseFocus by remember { mutableStateOf("") }
    var exerciseLengths by remember { mutableStateOf(emptyList<Int>()) }
    var workoutLength by remember { mutableStateOf("") }

    // Scaffold for the screen layout
    Scaffold(
        topBar = { SmallTopAppBar(navController, title = "Add Workout") },
        floatingActionButton= {
            FloatingActionButton(
                onClick = {
                    if (dayOfWorkout.isNotEmpty() && exerciseFocus.isNotEmpty() && selectedExercises.isNotEmpty()) {
                        val workoutWithExercises = WorkoutWithExercises(
                            dayOfWeek = dayOfWorkout,
                            focus = exerciseFocus,
                            exercises = selectedExercises
                        )
                        fitnessViewModel.addWorkoutWithExercises(workoutWithExercises)

                        val workoutsWithExercises = fitnessViewModel.getAllWorkoutsWithExercises()
                        fitnessViewModel.writeWorkoutsWithExercises(workoutsWithExercises)

                        navController.navigateUp()
                    } else {
                        coroutineScope.launch {
                            SnackbarHostState().showSnackbar(
                                message = "Please fill in all required fields",
                                actionLabel = "Dismiss",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            )
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.addExercise)
                )
            }
        },
        content = { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Display day of the week input
                Text(text = "Day of the week:")
                DayOfWeekInput(
                    values = availableDays.toTypedArray(),
                    modifier = Modifier
                        .padding(12.dp),
                    updateDayOfWeek = {
                        dayOfWorkout = it
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display focus input
                Text(text = "Focus:")
                FocusInput(
                    values = values,
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
                            selectedExercises = selectedExercises
                            isDialogVisible = false
                        },
                        dialogTitle = "Select Exercises",
                        onExerciseSelected = {
                            selectedExercises = it
                            isDialogVisible = false

                            workoutLength = fitnessViewModel.calculateWorkoutLength(selectedExercises, 0)
                        },
                        viewModel = fitnessViewModel
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
 * Function to add workout only on days that are not already scheduled
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
 * Function to display focus input based on available exercises
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


@Composable
fun ExerciseSelectionAlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String = "Select Exercises",
    onExerciseSelected: (List<Exercise>) -> Unit,
    viewModel: FitnessViewModel = viewModel()
) {
    val exercises = viewModel.readExercises()
    var selectedExercises by remember { mutableStateOf(emptyList<Exercise>()) }

    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = {
            if (exercises != null) {
                ExerciseSelectionDialogContent(
                    exercises = exercises,
                    selectedExercises = selectedExercises,
                    onSelectionChanged = { selectedExercises = it }
                )
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onExerciseSelected(selectedExercises) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Dismiss")
            }
        }
    )
}

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

        LazyColumn {
            items(exercises) { exercise ->
                ExerciseSelectionItem(exercise, selectedExercises, onSelectionChanged)
            }
        }
    }
}

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

@Composable
fun WorkoutLength(totalLength: String) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
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
