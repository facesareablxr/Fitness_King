package uk.ac.aber.dcs.cs31620.fitnessking.ui.components

import MainPageTopAppBar
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    MainPageNavigationDrawer(
        navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                // We know it will be open
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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
            bottomBar = {
                MainPageNavigationBar(navController)
            },
            floatingActionButton = floatingActionButton,
            snackbarHost = {
                snackbarHostState?.let {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        snackbarContent(data)
                    }
                }
            },
            content = { innerPadding ->
                pageContent(innerPadding)
            }
        )
    }
}