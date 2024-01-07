package uk.ac.aber.dcs.cs31620.fitnessking.ui.editing

import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold


@Composable
fun SavedExerciseScreen(navController: NavHostController) {
    TopLevelScaffold(
        navController = navController,
        pageContent = { innerPadding ->
            // Content of SavedExerciseScreen
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                // Display saved exercises here
                // ...
            }

            // Floating action button for navigation
            FloatingActionButton(
                onClick = {
                    navController.navigate("edit_exercise_screen") // Replace with actual route
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Exercise"
                )
            }
        }
    )
}