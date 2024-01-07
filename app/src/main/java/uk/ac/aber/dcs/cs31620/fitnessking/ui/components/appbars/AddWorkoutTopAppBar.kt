package uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.fitnessking.R

@Composable
fun AddWorkoutTopAppBar(navController: NavController) {
    TopAppBar(title = {
        Text(stringResource(id = R.string.addWorkout))
    },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.goBack)
                )
            }
        }
    )
}