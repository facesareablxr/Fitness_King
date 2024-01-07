package uk.ac.aber.dcs.cs31620.fitnessking.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import java.time.LocalDate

/**
 * Represents the home screen, has individual cards for each exercise for the current day.
 * @author Lauren Davis
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    TopLevelScaffold(
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeScreenContent(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun HomeScreenContent(modifier: Modifier = Modifier) {
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