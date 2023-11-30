package uk.ac.aber.dcs.cs31620.fitnessking.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Saved : Screen("saved")
    data object Schedule : Screen("schedule")
}

val screens = listOf(
    Screen.Home,
    Screen.Saved,
    Screen.Schedule
)