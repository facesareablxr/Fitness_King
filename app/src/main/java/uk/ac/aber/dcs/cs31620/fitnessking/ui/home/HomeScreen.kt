package uk.ac.aber.dcs.cs31620.fitnessking.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling.ExerciseCard
import java.time.LocalDate

@Composable
fun HomeScreen(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        pageContent = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                HomeScreenContent(
                    modifier = Modifier.padding(8.dp),
                    workoutViewModel = workoutViewModel,
                    navController
                )
            }
        }
    )
}

@Composable
fun HomeScreenContent(modifier: Modifier, workoutViewModel: WorkoutViewModel, navController: NavHostController) {
    val todaysWorkoutWithExercises by workoutViewModel.workoutWithExercises.observeAsState()

    Column(modifier = modifier.padding(8.dp)) {
        val currentDayCaps = LocalDate.now().dayOfWeek.name
        val currentDay = currentDayCaps.lowercase().replaceFirstChar { it.uppercase() }
        Text(
            text = currentDay,
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier
                .padding(bottom = 16.dp)
                .align(CenterHorizontally)
        )

        if (todaysWorkoutWithExercises != null) {
            val workout = todaysWorkoutWithExercises!!.keys.first()
            val exercises = todaysWorkoutWithExercises!![workout]!!
            WorkoutCardForEachExercise(exercises, modifier, navController)
        } else {
            // Handle the case where there's no workout for today
            Text(
                text = "No workout scheduled for today.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Handle random workout suggestion */ },
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Text("Suggest a random workout")
            }
        }
    }
}

@Composable
fun WorkoutCardForEachExercise(
    exercises: List<ExerciseEntity>,
    modifier: Modifier,
    navController: NavHostController
) {
    Column(modifier = modifier) {
        for (exercise in exercises) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Title for each exercise card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Exercise: ${exercise.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ExerciseCard(exercise = exercise, navController = navController)
                }
            }
        }
    }
}