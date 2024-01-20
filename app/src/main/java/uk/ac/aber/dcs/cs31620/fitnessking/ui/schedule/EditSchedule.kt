package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.DaysOfWeek
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Focus
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.SmallTopAppBar

@Composable
fun EditScheduleTopLevel(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel = viewModel()
){
    EditSchedule(
        navController = navController,
        updateWorkout = { workout ->
            workoutViewModel.updateWorkout(workout)
        }
    )
}


@Composable
fun EditSchedule(
    navController: NavHostController,
    updateWorkout: (WorkoutEntity) -> Unit = {},
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
                    updateWorkout(
                        DaysOfWeek.valueOf(dayOfWorkout), Focus.valueOf(exerciseFocus) , length, exerciseIds
                    ) { newWorkout ->
                        updateWorkout(newWorkout)
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
fun updateWorkout(
    dayOfWeek: DaysOfWeek,
    focus: Focus,
    length: Int,
    exerciseIds: List<Int>,
    doUpdate: (WorkoutEntity) -> Unit = {}
) {
    if(exerciseIds.isNotEmpty()){
        val workout = WorkoutEntity(
            workoutId = 0,
            day = dayOfWeek,
            focus = focus,
            length =  length
        )
        doUpdate(workout)
    }
}
