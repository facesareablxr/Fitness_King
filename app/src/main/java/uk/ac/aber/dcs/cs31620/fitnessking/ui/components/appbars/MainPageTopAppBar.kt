package uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme

/**
 * This is the main page top app bar, it has a burger button for the user to press and open the navigation menu.
 */
@Composable
fun MainPageTopAppBar(
    onClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.nav_drawer_menu)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
        scrollBehavior = scrollBehavior
    )
}

/**
 * This is just the preview for it
 */
@Preview
@Composable
private fun MainPageTopAppBarPreview() {
    FitnessKingTheme(dynamicColor = false) {
        MainPageTopAppBar()
    }
}