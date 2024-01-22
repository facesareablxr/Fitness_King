package uk.ac.aber.dcs.cs31620.fitnessking.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold

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

                }
            }
        )
    }
