package uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.IconGroup
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme

/**
 * This composable represents the main page navigation bar, offering options for home, schedule, and saved exercises.
 * The design is modeled after the FAA application, with adaptations for three navigation options.
 *
 * @param navController is the NavController for navigation
 */
@SuppressLint("SuspiciousIndentation")
@Composable
fun MainPageNavigationBar(navController: NavController) {
    // Map of icons for each screen
    val icons = mapOf(
        Screen.Home to IconGroup(
            filledIcon = Icons.Filled.Home,
            outlineIcon = Icons.Outlined.Home,
            label = stringResource(id = R.string.home)
        ),
        Screen.Schedule to IconGroup(
            filledIcon = Icons.Filled.CalendarMonth,
            outlineIcon = Icons.Outlined.CalendarMonth,
            label = stringResource(id = R.string.schedule)
        ),
        Screen.Saved to IconGroup(
            filledIcon = Icons.Filled.Favorite,
            outlineIcon = Icons.Outlined.Favorite,
            label = stringResource(id = R.string.favourite)
        )
    )

    // Navigation bar design and declaration
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Iterate through screens and create navigation items
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val labelText = icons[screen]!!.label

            NavigationBarItem(
                // Manages the icon changes
                icon = {
                    Icon(
                        imageVector = (
                                if (isSelected)
                                    icons[screen]!!.filledIcon
                                else
                                    icons[screen]!!.outlineIcon),
                        contentDescription = labelText
                    )
                },
                label = { Text(labelText) },
                selected = isSelected,
                // Manages the navigation
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Preview function
 */
@Preview
@Composable
private fun MainPageNavigationBarPreview() {
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        MainPageNavigationBar(navController)
    }
}