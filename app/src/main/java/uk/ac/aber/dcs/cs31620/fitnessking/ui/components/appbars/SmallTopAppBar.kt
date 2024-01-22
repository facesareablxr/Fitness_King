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
 * This is the small top app bar, it passes the title through its parameter to determine the
 * text in the bar
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
                    navController.navigateUp() // This will return to the previous screen
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.goBack) // This is just the back button
                )
            }
        }
    )
}

/**
 * This is just a preview
 */
@Preview
@Composable
private fun SmallTopAppBarPreview() {
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        SmallTopAppBar(navController = navController, title = "Hello!")
    }
}