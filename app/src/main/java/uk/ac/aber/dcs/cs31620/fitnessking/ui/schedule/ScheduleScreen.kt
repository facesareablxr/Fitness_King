package uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold

/**
 * Represents the home screen. For this version we only have a
 * top app bar and empty content.
 * @author Chris Loftus
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
            Text(text = "Schedule Screen",
                modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(){
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        ScheduleScreen(navController)
    }
}
