package uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme

/**
 * Composable for a small top app bar. It displays a title passed through its parameter and includes a back button.
 *
 * @param navController is the NavController for navigation control
 * @param title is the title text to be displayed in the app bar
 */
@Composable
fun SmallTopAppBar(navController: NavController, title: String) {
    TopAppBar(
        title = {
            Text(title)  // Display the passed title
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigateUp() // Navigate back to the previous screen
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.goBack) // Description for the back button
                )
            }
        }
    )
}

/**
 * Preview function
 */
@Preview
@Composable
private fun SmallTopAppBarPreview() {
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        SmallTopAppBar(navController = navController, title = "Hello!")
    }
}