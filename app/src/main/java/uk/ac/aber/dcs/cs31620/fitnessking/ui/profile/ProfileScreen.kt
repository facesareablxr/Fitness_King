package uk.ac.aber.dcs.cs31620.fitnessking.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.home.HomeScreenContent

/**
 * Represents the home screen. For this version we only have a
 * top app bar and empty content.
 * @author Chris Loftus
 */
@Composable
fun ProfileScreen(
        navController: NavHostController,
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

                        )
                }
            }
        )
    }

@Preview
@Composable
private fun HomeScreenPreview(){
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        ProfileScreen(navController)
    }
}
