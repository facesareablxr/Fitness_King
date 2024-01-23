package uk.ac.aber.dcs.cs31620.fitnessking.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.MainPageTopAppBar

/**
 * This is the top-level scaffold, defining layouts for most screens for consistency.
 * Avoids rewriting the same code for each screen when using the MainPageTopAppBar.
 *
 * @param navController is the NavHostController for navigation
 * @param floatingActionButton is a composable for adding a floating action button
 * @param snackbarContent is a composable for displaying Snackbar content
 * @param snackbarHostState is the SnackbarHostState for managing Snackbars
 * @param pageContent is a composable for defining the content of the screen
 * @param coroutineScope is the CoroutineScope for managing coroutines
 *
 * Taken from FAA: https://github.com/chriswloftus/feline-adoption-agency-v10/
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLevelScaffold(
    navController: NavHostController,
    floatingActionButton: @Composable () -> Unit = { },
    snackbarContent: @Composable (SnackbarData) -> Unit = {},
    snackbarHostState: SnackbarHostState? = null,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {},
    coroutineScope: CoroutineScope
) {
    // Remember the state of the drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // This adds the MainPageNavigationDrawer for use with the MainPageTopAppBar
    MainPageNavigationDrawer(
        navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                drawerState.close()
            }
        }
    ) {
        // Scaffold composable for building the overall screen structure
        Scaffold(
            topBar = {
                // MainPageTopAppBar displayed at the top of the page
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                // This will open the MainPageNavigationDrawer
                MainPageTopAppBar(onClick = {
                    coroutineScope.launch {
                        if (drawerState.isOpen) {
                            drawerState.close()
                        } else {
                            drawerState.open()
                        }
                    }
                },
                    scrollBehavior = scrollBehavior
                )
            },
            // MainPageNavigationBar added to the bottom of the screen
            bottomBar = {
                MainPageNavigationBar(navController)
            },
            // floatingActionButton added to the screen
            floatingActionButton = floatingActionButton,
            // Snackbar host for displaying Snackbars
            snackbarHost = {
                snackbarHostState?.let {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        snackbarContent(data)
                    }
                }
            },
            // Content area defined by [pageContent]
            content = { innerPadding ->
                pageContent(innerPadding)
            }
        )
    }
}