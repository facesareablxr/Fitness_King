package uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation

/**
 * This is the list of screens in the program and the routes that NavController should take
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Schedule : Screen("schedule")
    data object Saved: Screen("saved")
    data object AddExercise : Screen("addExercise")
    data object EditExercise : Screen("editExercise")
    data object AddWorkout : Screen("addWorkout")
    data object  EditWorkout : Screen ("editWorkout")
}

/**
 * This is the list of screens, this is used in the navigation bar at the bottom of the page
 */
val screens = listOf(
    Screen.Home,
    Screen.Schedule,
    Screen.Saved
)