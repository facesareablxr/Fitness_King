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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.workout.WorkoutViewModel
import java.time.LocalDate

/**
 * Represents the home screen, has individual cards for each exercise for the current day.
 * @author Lauren Davis
 */
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
                    workoutViewModel = workoutViewModel
                    )
            }
        }
    )
}

@Composable
fun HomeScreenContent(modifier: Modifier = Modifier, workoutViewModel: WorkoutViewModel) {
   Column(modifier = Modifier.padding(8.dp)) {
            val currentDayCaps = LocalDate.now().dayOfWeek.name
            val currentDay = currentDayCaps.lowercase().replaceFirstChar { it.uppercase() }
            Text(
                text = currentDay,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(CenterHorizontally)
            )
       WorkoutCardForToday(workoutViewModel = workoutViewModel)
    }
}

@Composable
fun WorkoutCardForToday(
    workoutViewModel: WorkoutViewModel,
   // onStartWorkout: () -> Unit,
    //modifier: Modifier = Modifier
) {
    val currentWorkout = workoutViewModel.currentWorkout.value
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (currentWorkout != null) {
                Text(
                    text = "Your workout for today:",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Display exercises for the workout here
            } else {
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
}



@Preview
@Composable
private fun HomeScreenPreview(){
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        HomeScreen(navController)
    }
}