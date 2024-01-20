@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen


/**
 * Represents the Schedule Screen which will display each workout scheduled for the week, it will
 * allow the user to select the card to view the workout in more detail
 * @author Lauren Davis
 */
@Composable
fun ScheduleScreenTopLevel(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel = viewModel(),
) {
    val workoutsList by workoutViewModel.workoutList.observeAsState(listOf())

    ScheduleScreen(navController, workoutViewModel, workoutsList)
}

@Composable
fun ScheduleScreen(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    workoutsList: List<WorkoutEntity>,
) {
    val coroutineScope = rememberCoroutineScope()

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
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                ScheduleScreenContent(workoutViewModel) {
                    coroutineScope.launch {
                    }
                }
            }
        }
    )
}

/**
 * Function to assemble the content for the screen
 */
@Composable
fun ScheduleScreenContent(
    workoutViewModel: WorkoutViewModel,
    onWorkoutClick: (Long) -> Unit,
) {
    val workoutsWithExercises by workoutViewModel.allWorkoutsWithExercises.observeAsState()

    Column {
        workoutsWithExercises?.forEach { (workout, _) ->
            val exercisesState = remember { workoutViewModel.getExercisesForWorkout(workout.workoutId.toLong()) }
            val exercises = exercisesState.value
            WorkoutCard(
                workout = workout,
                exercises = exercises ?: emptyList(),
                onWorkoutClick = onWorkoutClick
            )
        }
    }
}

/**
 * Cards for the workouts, will include the opportunity to click on them
 */
@Composable
fun WorkoutCard(
    workout: WorkoutEntity,
    exercises: List<ExerciseEntity>,
    onWorkoutClick: (Long) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { onWorkoutClick(workout.workoutId.toLong()) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = workout.day.toString(), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Focus: ${workout.focus}")
            Spacer(modifier = Modifier.height(16.dp))


                Text(text = "Exercises:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

            exercises.forEach { exercise ->
                Text(
                    text = "${exercise.name} - ${exercise.sets} sets x ${exercise.reps} reps @ ${exercise.weight} kg"
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            }
        }
    }



/**
 * Function to add images in for the cards
 */
@Composable
fun ExerciseImage(imageRes: Int) {
    androidx.compose.foundation.Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop
    )
}

