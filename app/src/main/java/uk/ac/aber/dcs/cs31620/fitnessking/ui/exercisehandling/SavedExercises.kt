package uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Exercise
import uk.ac.aber.dcs.cs31620.fitnessking.model.datafiles.FitnessViewModel
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
 * @param fitnessViewModel is the viewModel set up to manage csv/text files
 *
 */

@Composable
fun SavedExerciseTopLevel(
    navController: NavHostController,
    fitnessViewModel: FitnessViewModel = viewModel()
) {
    var exerciseName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var exercises by remember {
        mutableStateOf<List<Exercise>>(emptyList())
    }

    LaunchedEffect(key1 = Unit) {
        try {
            val result = fitnessViewModel.readExercises()
            coroutineScope.launch {
                exercises = result
            }
        } catch (e: Exception) {
            // Handle the exception or log it
        }
    }
    var searchQuery by remember { mutableStateOf("") }
    var isFavouritesOnly by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    SavedExerciseScreen(
        modifier = Modifier.padding(8.dp),
        navController = navController,
        exerciseList = exercises,
        searchQuery = searchQuery,
        isFavouritesOnly = isFavouritesOnly,
        onSearchQueryChanged = { searchQuery = it },
        onFavouritesFilterChanged = { isFavouritesOnly = it },
        onToggleFilterDropdown = { expanded = !expanded },
        expanded = expanded,
        onDelete = { deletedExerciseName ->
            // Update the state when an exercise is deleted
            exercises = exercises.filter { it.name != deletedExerciseName }
        }
    )
}



/**
 * This is the saved exercise screen, it lays out the scaffold for the screen and lays out the
 * key elements for the screen.
 * @param navController is the same as before
 * @param exerciseList is the list of exercises in the format defined in the Exercise data class
 * @param searchQuery is the query input by the user
 * @param isFavouritesOnly is the filter function which reduces the list down to only the favourites
 * @param onSearchQueryChanged updates the query as it is changed by the user
 * @param onFavouritesFilterChanged updates the filter
 * @param onToggleFilterDropdown updates whether the filter options are chosen or not
 * @param expanded is for the dropdown menu, is it closed or open
 */
@Composable
fun SavedExerciseScreen(
    modifier: Modifier,
    navController: NavHostController,
    exerciseList: List<Exercise>,
    searchQuery: String,
    isFavouritesOnly: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onFavouritesFilterChanged: (Boolean) -> Unit,
    onToggleFilterDropdown: () -> Unit,
    expanded: Boolean,
    onDelete: (String) -> Unit,
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
                    //This is the search bar
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
                        FilterButton(onToggleFilterDropdown, expanded, onFavouritesFilterChanged)
                    }
                }
                SavedExerciseContent(
                    exerciseList,
                    navController,
                    searchQuery,
                    isFavouritesOnly,
                    onDelete
                )

            }
        }
    )
}



/**
 * This is the SavedExerciseContent screen where the content for the screen is being defined.
 * @param exercises same as before,
 * @param navController same as before,
 * @param searchQuery same as before,
 * @param isFavouritesOnly same as before,
 * @param onFavouriteClicked same as before
 */
@Composable
fun SavedExerciseContent(
    exercises: List<Exercise>,
    navController: NavHostController,
    searchQuery: String,
    isFavouritesOnly: Boolean,
    onDelete: (String) -> Unit
) {
    val filteredExercises = exercises.filter { exercise ->
        exercise.name.contains(searchQuery, ignoreCase = true) &&
                (!isFavouritesOnly || exercise.isFavourite)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(filteredExercises) { exercise ->
            ExerciseCard(exercise, navController, onDelete)
        }
    }
}

/**
 * This is the filter button, so far only has 2 filters, favourite and all exercises.
 */
@Composable
private fun FilterButton(
    onToggleFilterDropdown: () -> Unit,
    expanded: Boolean,
    onFavouritesFilterChanged: (Boolean) -> Unit
) {
    IconButton(
        onClick = onToggleFilterDropdown,
        modifier = Modifier
            .size(50.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
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


/**
 * This is the ExerciseCard class where the cards for each exercise are constructed using the information
 * stored in the database.
 * @param exercise is the same as before,
 * @param navController is the same as before,
 * @param onFavouriteClicked is the same as before.
 */
@Composable
fun ExerciseCard(
    exercise: Exercise,
    navController: NavHostController,
    onDelete: (String) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    // This is the card itself
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
            // Leading image
            val painter = rememberAsyncImagePainter(model = exercise.image)
            Image(
                painter = painter,
                contentDescription = "Exercise Image",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            // This is the details for the card, only basic sets | reps | weight
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Sets: ${exercise.sets} | Reps: ${exercise.reps} | Weight: ${exercise.weight} kg",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            // This is handling the favourites
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                //onFavouriteClicked(exercise)
            }) {
                Icon(
                    imageVector = if (exercise.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favourite"
                )
            }
        }

        ExerciseCardExpandedDetails(showDialog, exercise, navController, onDelete)

        // Delete dialog with its own showDialog value
        if (showDeleteDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog.value = false },
                title = {
                    Text(text = "Delete Exercise")
                },
                text = {
                    Text(text = "Are you sure you want to delete this exercise?")
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        TextButton(onClick = { showDeleteDialog.value = false }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = {
                            // Handle delete action
                            showDeleteDialog.value = false
                        }) {
                            Text("Delete")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Bottom)
            )
        }
    }
}

@Composable
private fun ExerciseCardExpandedDetails(
    showDialog: MutableState<Boolean>,
    exercise: Exercise,
    navController: NavHostController,
    onDelete: (String) -> Unit
) {
    val fitnessViewModel: FitnessViewModel = viewModel()
    val showDropdown = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    // Exercise content, including image, rest time, focus, and approximate time
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Focus: ${exercise.focus}")
                        Spacer(modifier = Modifier.height(4.dp))
                        // Image
                        val painter = rememberAsyncImagePainter(model = exercise.image)
                        Image(
                            painter = painter,
                            contentDescription = "Exercise Image",
                            modifier = Modifier
                                .width(150.dp)
                                .height(150.dp)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Other details
                        Text(text = "Sets: ${exercise.sets}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Reps: ${exercise.reps}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Weight: ${exercise.weight} kg")
                        Spacer(modifier = Modifier.height(4.dp))
                        if (exercise.isDropSet) {
                            Text(text = "Drop Set")
                        }

                        // Additional details
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Rest: ${exercise.restTime} minutes")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Approximate time per set: ${exercise.length} minutes")
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {}
                ) {
                    TextButton(onClick = {
                        // Handle close action
                        showDialog.value = false
                    }) {
                        Text("Close")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        // Call onDelete when the Delete button is clicked
                        onDelete(exercise.name)
                        showDialog.value = false
                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                        Text("Delete")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}

