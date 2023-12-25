package uk.ac.aber.dcs.cs31620.fitnessking.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
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
import uk.ac.aber.dcs.cs31620.fitnessking.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.fitnessking.ui.navigation.screens
import uk.ac.aber.dcs.cs31620.fitnessking.ui.theme.FitnessKingTheme

@Composable
fun MainPageNavigationBar(navController: NavController){
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

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach{screen ->
            val isSelected = currentDestination?.hierarchy?.any{it.route == screen.route} == true
            val labelText = icons[screen]!!.label
            NavigationBarItem(
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
                label = { Text(labelText)},
                selected = isSelected,
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

@Preview
@Composable
private fun MainPageNavigationBarPreview() {
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        MainPageNavigationBar(navController)
    }
}