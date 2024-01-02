package uk.ac.aber.dcs.cs31620.fitnessking.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.WorkoutEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.viewmodel.WorkoutViewModel
import java.time.LocalDate
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.dao.ExerciseDao as ExerciseDao

/**
 * Represents the home screen, has individual cards for each exercise for the current day.
 * @author Lauren Davis
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel = viewModel(),
) {
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val workoutForToday = workoutViewModel.workoutForToday
            HomeScreenContent(
                modifier = Modifier.padding(8.dp),
                workout = workoutForToday,
            )
        }
    }
}

@Composable
fun HomeScreenContent(modifier: Modifier = Modifier, workout: WorkoutEntity?) {
   Column(modifier = Modifier.padding(8.dp)) {
            // Display current day as title
            val currentDayCaps = LocalDate.now().dayOfWeek.name
            val currentDay = currentDayCaps.lowercase().replaceFirstChar { it.uppercase() }
            Text(
                text = currentDay,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
                    .align(CenterHorizontally)
            )
        // Main workout card
       WorkoutSummaryCard(workout)
    }
}

@Composable
fun WorkoutSummaryCard(workout: WorkoutEntity?) {
    var totalLength by remember { mutableIntStateOf(0) }
    LaunchedEffect(workout) {
        val exercise  = ExerciseDao()
        val calculatedLength = workout?.calculateTotalLength(exercise) ?: 0
        totalLength = calculatedLength
    }
    workout?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .wrapContentHeight()
                .clickable { /* Handle card click if needed */ }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Focus: ${workout.focus}", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = "Total Length: $totalLength minutes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCardList(workout: WorkoutEntity?) {
    workout?.exercises?.let { exercises ->
        LazyColumn {
            items(exercises) { exercise ->
                ExerciseCard(exercise = exercise)
            }
        }
    }
}

// Assuming ExerciseEntity has name, imageUri, sets, reps, weight, and length
@Composable
fun ExerciseCard(exercise: ExerciseEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Image(
                painter = rememberImagePainter(
                    data = exercise.imageUri
                ),
                contentDescription = "Exercise Image",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = exercise.name, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "${exercise.sets} sets x ${exercise.reps} reps",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${exercise.weight} kg",
                    style = MaterialTheme.typography.bodyMedium
                )
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
