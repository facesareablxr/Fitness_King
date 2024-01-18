package uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Schedule : Screen("schedule")
    data object Saved: Screen("saved")
    data object AddExercise : Screen("addExercise")
    data object EditExercise : Screen("editExercise")

}

val screens = listOf(
    Screen.Home,
    Screen.Schedule,
    Screen.Saved
)