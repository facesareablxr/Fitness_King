@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Workout

val weeklyWorkouts = listOf(
    Workout("Monday", "Push-ups","Chest"),
    Workout("Tuesday", "Squats","Legs"),
    Workout("Wednesday", "Plank","Core"),
    Workout("Thursday", "Pull-ups","Back"),
    Workout("Friday", "Jumping Jacks","Cardio"),
    Workout("Saturday", "Crunches","Abs"),
    Workout("Sunday", "Rest","Rest")
)

/**
 * Represents the Schedule Screen which will display each workout scheduled for the week, it will
 * allow the user to select the card to view the workout in more detail
 * @author Lauren Davis
 */
@Composable
fun ScheduleScreen(navController: NavHostController) {
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ScheduleScreenContent()
        }
    }
}

/**
 * Cards for the workouts, will include the opportunity to click on them
 */
@Composable
fun WorkoutCard(workout: Workout, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = workout.day, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = workout.exerciseName)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Focus: ${workout.focus}", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            //ExerciseImage(imageRes = workout.exerciseImageRes)
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

/**
 * Function to assemble the content for the screen
 */
@Composable
fun ScheduleScreenContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(weeklyWorkouts) { workout ->
            WorkoutCard(workout = workout) {
                // Navigate to detail screen when the card is clicked
                // Pass workout details to the detail screen if needed
                // Replace `DetailScreen` with your actual destination
            }
        }
    }
}


@Preview
@Composable
private fun ScheduleScreenPreview(){
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        ScheduleScreen(navController)
    }
}
