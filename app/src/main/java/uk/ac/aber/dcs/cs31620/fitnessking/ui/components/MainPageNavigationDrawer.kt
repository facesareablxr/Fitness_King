package uk.ac.aber.dcs.cs31620.fitnessking.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen

/**
 * This is the navigation drawer, this will be accessible through the MainPageTopAppBar
 */
@Composable
fun MainPageNavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val items = listOf(
        ItemData(Icons.Default.Home, stringResource(R.string.home), Screen.Home.route),
        ItemData(Icons.Default.CalendarMonth, stringResource(R.string.schedule), Screen.Schedule.route),
        ItemData(Icons.Default.Favorite, stringResource(R.string.favourite), Screen.Saved.route),
        ItemData(Icons.Default.Settings, stringResource(R.string.settings), "settings_screen_route")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val selectedItem = rememberSaveable { mutableIntStateOf(0) }
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height((26.dp)))
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(50.dp)
                        .fillMaxWidth()
                        .align(CenterHorizontally),
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.username), modifier = Modifier.padding(16.dp)) },
                    selected = false,
                    onClick = { /* Handle profile click */ }
                )
                Divider(
                    modifier = Modifier
                        .padding(20.dp, 0.dp)
                        .align(CenterHorizontally),
                )
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp, 0.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = index == selectedItem.intValue,
                            onClick = {
                                selectedItem.intValue = index
                                if (index == 0) {
                                    closeDrawer()
                                } else {
                                    navController.navigate(item.route)
                                    closeDrawer()
                                }
                            }
                        )
                    }
                }
            }
        },
        content = content
    )
}

data class ItemData(
    val icon: ImageVector,
    val label: String,
    val route: String
)