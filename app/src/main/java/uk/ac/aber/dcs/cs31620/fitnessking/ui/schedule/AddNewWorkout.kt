package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.SmallTopAppBar
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.ButtonSpinner

@Composable
fun AddNewWorkoutTopLevel(
     navController: NavHostController,
     workoutViewModel: WorkoutViewModel = viewModel()
){
    AddNewWorkout(
        navController = navController,
        insertWorkout = { workout ->
            workoutViewModel.insertWorkout(workout)
        }
    )
}


@Composable
fun AddNewWorkout(
    navController: NavHostController,
    insertWorkout: (WorkoutEntity) -> Unit = {} ,
    workoutViewModel: WorkoutViewModel = viewModel()
) {
    //Assigning the values for the date and focus arrays
    var values = stringArrayResource(id = R.array.DayOfWeek)
    val dayOfWeekValues = values.copyOfRange(1, values.size)
    values = stringArrayResource(id = R.array.Focus)
    val focusValues = values.copyOfRange(1, values.size)
    val availableDays = workoutViewModel.availableDays.observeAsState()


    //Assigning the variables for the workout components
    var dayOfWorkout by remember { mutableStateOf(dayOfWeekValues[0]) }
    var exerciseFocus by remember { mutableStateOf(focusValues[0]) }
    var length by remember { mutableIntStateOf(60) }
    var exerciseIds by remember { mutableStateOf(emptyList<Int>()) }
    var exerciseLengths by remember { mutableStateOf(emptyList<Int>()) }
    var rest by remember { mutableIntStateOf(5) } // Initial rest time
    //
    Scaffold(
        //
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    insertWorkout(
                        DaysOfWeek.valueOf(dayOfWorkout), Focus.valueOf(exerciseFocus) , length, exerciseIds
                    ) { newWorkout ->
                        insertWorkout(newWorkout)
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(id = R.string.addWorkoutButton)
                )
            }
        },
        // Separated top app bar, under appbars package
        topBar = {
            SmallTopAppBar(navController, title = "Add Workout" )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .wrapContentWidth()
        ) {
            Text(text = "Day of the week:")
            DayOfWeekInput(
                values = dayOfWeekValues,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .wrapContentWidth(),
                updateDayOfWeek = {
                    dayOfWorkout = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Focus:")
            FocusInput(
                values = focusValues,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .wrapContentWidth(),
                updateFocus = {
                    exerciseFocus = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
            //    ExerciseSelectionAlertDialog()
            }) {
                Text("Select Exercises")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Rest time:")
            RestTime(
                modifier = Modifier,
                restTimeInMinutes = 0,
                onRestChange = {
                    rest = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            WorkoutLength(
                exerciseLengths = exerciseLengths,
                restTime = rest )
        }
    }
}

/**
 * Function to insert workout into the workout database
 */
fun insertWorkout(
    dayOfWeek: DaysOfWeek,
    focus: Focus,
    length: Int,
    exerciseIds: List<Int>,
    doInsert: (WorkoutEntity) -> Unit = {}
) {
    if(exerciseIds.isNotEmpty()){
        val workout = WorkoutEntity(
            workoutId = 0,
            day = dayOfWeek,
            focus = focus,
            length =  length
        )
        doInsert(workout)
    }
}

/**
 * Function to add the information for the day of the week
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
        }
    )
}

/**
 * Function to add what the focus of the exercise is
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
        }
    )
}

/**
 * This is the exercise selection box, originally, I had tried to attempt to do it so that you could
 * press + and - to add and remove more exercises, but due to time constraints, I had to simplify.
 */
@Composable
fun ExerciseSelectionAlertDialog(
    onDismissRequest: () -> Unit,
    onExerciseSelected: (List<ExerciseEntity>) -> Unit,
    exerciseViewModel: ExerciseViewModel = viewModel(),
    dialogTitle: String = "Select Exercises",
    selectedExercises: List<ExerciseEntity> = emptyList()
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = {
            ExerciseSelectionDialogContent(
                onExerciseSelected,
                exerciseViewModel,
                onDismissRequest
            )
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

/**
 * This function is to display what exercises are available for the user to select to undertake
 */
@Composable
private fun ExerciseSelectionDialogContent(
    onExerciseSelected: (List<ExerciseEntity>) -> Unit,
    exerciseViewModel: ExerciseViewModel,
    onDismissRequest: () -> Unit
) {
    val exercises by exerciseViewModel.allExercises.observeAsState(emptyList())
    var selectedExercises by remember { mutableStateOf(emptyList<ExerciseEntity>()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Select Exercises")

        LazyColumn {
            items(exercises) { exercise ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Toggle exercise selection
                            selectedExercises = if (selectedExercises.contains(exercise)) {
                                selectedExercises - exercise
                            } else {
                                selectedExercises + exercise
                            }
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedExercises.contains(exercise),
                        onCheckedChange = null // Disable direct checkbox interaction
                    )
                    Text(text = exercise.name, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

/**
 * Function to allow the user to dictate how long they want to rest between sets
 */
@Composable
fun RestTime(
    restTimeInMinutes: Int,
    onRestChange: (Int) -> Unit,
    modifier: Modifier
) {
    var restTime by remember { mutableIntStateOf(restTimeInMinutes) }

    Slider(
        value = restTime.toFloat(),
        onValueChange = { newValue ->
            restTime = newValue.toInt()
            onRestChange(restTime)
        },
        valueRange = 0f..30f,
        steps = 31,
        onValueChangeFinished = {},
        modifier = modifier.width(350.dp)
    )
    Text(
        text = "Rest time: $restTime minutes",
        modifier = Modifier.padding(start = 16.dp)
    )
}

/**
 * Displays the length of the workout on a container
 */
@Composable
fun WorkoutLength(
    exerciseLengths: List<Int>,
    restTime: Int
) {
    val totalLength = calculateTotalWorkoutLength(exerciseLengths, restTime)

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
            Text(
                text = totalLength.minutesToHoursMinutes()
            )
        }
    }
}

/**
 * Returns the time in minutes or minutes and hours, depending on how long the workout actually is
 */
fun Int.minutesToHoursMinutes(): String {
    val hours = this / 60
    val minutes = this % 60
    return if (hours > 0) {
        "$hours hours $minutes minutes"
    } else {
        "$minutes minutes"
    }
}

/**
 * Calculates the total amount of time for the whole workout
 */
fun calculateTotalWorkoutLength(exerciseLengths: List<Int>, restTime: Int): Int {
    if (exerciseLengths.isEmpty()) return 0
    val totalExerciseTime = exerciseLengths.sum()
    val totalRestTime = restTime * (exerciseLengths.size - 1)
    return totalExerciseTime + totalRestTime
}