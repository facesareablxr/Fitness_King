package uk.ac.aber.dcs.cs31620.fitnessking.ui.components

import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.MainPageTopAppBar
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * This is the top level scaffold, it defines the layouts for most screens for consistency
 * and not having to rewrite the same code for each screen when using the uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.MainPageTopAppBar
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
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // This adds the MainPageNavigationDrawer for use with the uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.MainPageTopAppBar
    MainPageNavigationDrawer(
        navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                // This is the uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.MainPageTopAppBar and will be at the top of the page
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
            // This adds the navigation bar to the bottom of the screen
            bottomBar = {
                MainPageNavigationBar(navController)
            },
            // This allows for the addition of a floatingactionbutton
            floatingActionButton = floatingActionButton,
            snackbarHost = {
                snackbarHostState?.let {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        snackbarContent(data)
                    }
                }
            },
            // This is where the content is defined
            content = { innerPadding ->
                pageContent(innerPadding)
            }
        )
    }
}