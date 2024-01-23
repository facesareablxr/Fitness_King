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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling.AddNewExerciseTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling.SavedExerciseTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.home.HomeScreen
import uk.ac.aber.dcs.cs31620.fitnessking.ui.profile.ProfileScreen
import uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule.AddNewWorkoutTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule.ScheduleScreenTopLevel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.settings.SettingsScreen

/**
 * Starting actibity point, made using first tutorial in the year
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessKingTheme(dynamicColor = false) {
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
private fun BuildNavigationGraph() {
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
        composable(Screen.Profile.route){ ProfileScreen(navController)}
        composable(Screen.Settings.route){ SettingsScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FitnessKingTheme(dynamicColor = false) {
        BuildNavigationGraph()
    }
}

