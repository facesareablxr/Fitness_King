package uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen

/**
 * This is the saved exercise screen where the user can view, edit, favourite, search, filter
 * their saved exercises. From here, they can add new ones by clicking on the floating action button
 * or editing a saved one by clicking on it, this will load a dialog where they can select to edit it.
 * @author Lauren Davis
 */

/**
 * This is the top level of the class, this manages the calling of saved exercise screen and is used
 * to call the screen in the navigation graph.
 * @param navController is the NavHostController, it links the screen with the navigation graph
 * @param exerciseViewModel is the ExerciseViewModel from the database and manages the execution of
 * various queries or actions in the database
 */

@Composable
fun SavedExerciseTopLevel(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel = viewModel(),
) {
    var exerciseList by remember {mutableStateOf(listOf<ExerciseEntity>())}
    var searchQuery by remember { mutableStateOf("") }
    var isFavouritesOnly by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    SavedExerciseScreen(
        navController = navController,
        exerciseList = exerciseList,
        searchQuery = searchQuery,
        isFavouritesOnly = isFavouritesOnly,
        onSearchQueryChanged = { searchQuery = it },
        onFavouritesFilterChanged = { isFavouritesOnly = it },
        onToggleFilterDropdown = { expanded = !expanded },
        expanded = expanded
    )
}




/**
 * This is the saved exercise screen, it lays out the scaffold for the screen and lays out the
 * key elements for the screen.
 * @param navController is the same as before
 * @param exerciseList is the list of exercises in the format defined in ExerciseEntity
 * @param searchQuery is the query input by the user
 * @param isFavouritesOnly is the filter function which reduces the list down to only the favourites
 * @param onSearchQueryChanged updates the query as it is changed by the user
 * @param onFavouritesFilterChanged updates the filter
 * @param onToggleFilterDropdown updates whether the filter options are chosen or not
 * @param expanded is for the dropdown menu, is it closed or open
 * @param exerciseViewModel same as before.
 */
@Composable
fun SavedExerciseScreen(
    navController: NavHostController,
    exerciseList: List<ExerciseEntity>,
    searchQuery: String,
    isFavouritesOnly: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onFavouritesFilterChanged: (Boolean) -> Unit,
    onToggleFilterDropdown: () -> Unit,
    expanded: Boolean,
    exerciseViewModel: ExerciseViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    TopLevelScaffold(
        coroutineScope = coroutineScope,
        navController = navController,
        floatingActionButton = {
            //This is the floating action button which allows the user to navigate to the AddExercise screen
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddExercise.route)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Add exercise",
                            actionLabel = "Undo"
                        )
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.addExercise)
                )
            }
        },
        pageContent = {
            Column(modifier = Modifier.padding(it)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //This is the search bar creation/definition
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        label = { Text("Search") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                        modifier = Modifier
                            .padding(12.dp)
                            .width(325.dp)
                            .height(50.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        // Filter button with only the icon
                        IconButton(
                            onClick = onToggleFilterDropdown,
                            modifier = Modifier
                                .size(50.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter"
                            )
                        }
                        // Dropdown menu for the filter with its options
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { onToggleFilterDropdown() }
                        ) {
                            DropdownMenuItem(onClick = { onFavouritesFilterChanged(true) }) {
                                Text("Favourites")
                            }
                            DropdownMenuItem(onClick = { onFavouritesFilterChanged(false) }) {
                                Text("All Exercises")
                            }
                        }
                    }
                }
                SavedExerciseContent(
                    exerciseList = exerciseList,
                    navController = navController,
                    searchQuery = searchQuery,
                    isFavouritesOnly = isFavouritesOnly,
                    onFavouriteClicked = { exercise ->
                       // exerciseViewModel.toggleFavourite(exercise)
                    },
                )
            }
        }
    )
}

/**
 * This is the SavedExerciseContent screen where the content for the screen is being defined.
 * @param exerciseList same as before,
 * @param navController same as before,
 * @param searchQuery same as before,
 * @param isFavouritesOnly same as before,
 * @param onFavouriteClicked same as before
 */
@Composable
fun SavedExerciseContent(
    exerciseList: List<ExerciseEntity>,
    navController: NavHostController,
    searchQuery: String,
    isFavouritesOnly: Boolean,
    onFavouriteClicked: (ExerciseEntity) -> Unit
) {
    //This creates the cards for the filtered exercises only
    val filteredExercises = exerciseList.filter { exercise ->
        exercise.name.contains(searchQuery, ignoreCase = true) &&
                (!isFavouritesOnly || exercise.isFavourite)
    }
    //This creates exercise cards for each exercise stored in the database
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(filteredExercises) { exercise ->
            ExerciseCard(
                exercise = exercise,
                navController = navController,
                onFavouriteClicked = onFavouriteClicked
            )
        }
    }
}

/**
 * This is the ExerciseCard class where the cards for each exercise are constructed using the information
 * stored in the database.
 * @param exercise is the same as before,
 * @param navController is the same as before,
 * @param onFavouriteClicked is the same as before.
 */
@Composable
fun ExerciseCard(
    exercise: ExerciseEntity,
    navController: NavHostController,
    onFavouriteClicked: (ExerciseEntity) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { showDialog.value = true },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Sets: ${exercise.sets} | Reps: ${exercise.reps} | Weight: ${exercise.weight}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                onFavouriteClicked(exercise)
            }) {
                Icon(
                    imageVector = if (exercise.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favourite"
                )
            }

        }
        // This is where the dialog box is created, showing all the exercise information and allowing the user
        // to select to continue on to edit the exercise.
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                },
                text = {
                    Column {
                        Text(text = "Sets: ${exercise.sets}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Reps: ${exercise.reps}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Weight: ${exercise.weight}")
                        Spacer(modifier = Modifier.height(8.dp))
                        if (exercise.isDropSet) {
                            Text(text = "Drop Set")
                        }
                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text("Close")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = {
                            navController.navigate(
                                Screen.EditExercise.route
                            )
                            showDialog.value = false
                        }) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                            Text("Edit")
                        }
                    }
                },
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Bottom)
            )
        }
    }
}
