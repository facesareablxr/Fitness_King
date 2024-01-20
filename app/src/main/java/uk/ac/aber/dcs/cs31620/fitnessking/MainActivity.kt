package uk.ac.aber.dcs.cs31620.fitnessking
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling.AddNewExerciseTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.home.HomeScreen
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling.EditExerciseTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling.SavedExerciseTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule.AddNewWorkoutTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule.ScheduleScreenTopLevel

/**
 * Starting activity class. Entry point for the app.
 * @author Chris Loftus
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessKingTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph(
    exerciseViewModel: ExerciseViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Schedule.route) { ScheduleScreenTopLevel(navController) }
        composable(Screen.Saved.route) { SavedExerciseTopLevel(navController) }
        composable(Screen.AddExercise.route){ AddNewExerciseTopLevel(navController) }
        composable(Screen.AddWorkout.route){ AddNewWorkoutTopLevel(navController)}
        composable(Screen.EditExercise.route){ EditExerciseTopLevel(navController)}
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FitnessKingTheme(dynamicColor = false) {
        BuildNavigationGraph()
    }
}