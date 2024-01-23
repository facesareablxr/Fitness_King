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
 * This file contains the implementation for the "Saved Exercise" screen, where users can view, edit, favorite,
 * search, filter their saved exercises. They can also add new exercises or edit existing ones by clicking on
 * the floating action button or the exercise card, respectively.
 */

/**
 * Top-level composable function.
 * @param navController is the NavHostController, linking the screen with the navigation graph
 * @param fitnessViewModel is the viewModel set up to manage csv/text files
 */
@Composable
fun SavedExerciseTopLevel(
    navController: NavHostController,
    fitnessViewModel: FitnessViewModel = viewModel()
) {
    // Initialization of variables and fetching saved exercises from the view model
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
            println("No exercises found")
        }
    }

    // State variables for search, filter, and filter dropdown
    var searchQuery by remember { mutableStateOf("") }
    var isFavouritesOnly by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    // Calling the Saved Exercise screen
    SavedExerciseScreen(
        navController = navController,
        exerciseList = exercises,
        searchQuery = searchQuery,
        isFavouritesOnly = isFavouritesOnly,
        onSearchQueryChanged = { searchQuery = it },
        onFavouritesFilterChanged = { isFavouritesOnly = it },
        onToggleFilterDropdown = { expanded = !expanded },
        expanded = expanded,
        onDelete = { deletedExerciseName ->
            exercises = exercises.filter { it.name != deletedExerciseName }
        },
        fitnessViewModel = fitnessViewModel
    )
}

/**
 * Composable function representing the Saved Exercise screen. Users can view, filter, and search for
 * their saved exercises. It also provides the option to add new exercises.
 * @param navController is the NavHostController, linking the screen with the navigation graph
 * @param exerciseList is the list of exercises in the format defined in the Exercise data class
 * @param searchQuery is the query input by the user
 * @param isFavouritesOnly is the filter function that reduces the list down to only the favorites
 * @param onSearchQueryChanged updates the query as it is changed by the user
 * @param onFavouritesFilterChanged updates the filter
 * @param onToggleFilterDropdown updates whether the filter options are chosen or not
 * @param expanded is for the dropdown menu, indicating if it is closed or open
 */
@Composable
fun SavedExerciseScreen(
    navController: NavHostController,
    exerciseList: List<Exercise>,
    searchQuery: String,
    isFavouritesOnly: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onFavouritesFilterChanged: (Boolean) -> Unit,
    onToggleFilterDropdown: () -> Unit,
    expanded: Boolean,
    onDelete: (String) -> Unit,
    fitnessViewModel: FitnessViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Setting up the top-level scaffold
    TopLevelScaffold(
        coroutineScope = coroutineScope,
        navController = navController,
        floatingActionButton = {
            // Floating action button for adding a new exercise
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
            // Main content of the Saved Exercise screen
            Column(modifier = Modifier.padding(it)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Search bar
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
                // Content displaying saved exercises
                SavedExerciseContent(
                    exerciseList,
                    searchQuery,
                    isFavouritesOnly,
                    onDelete,
                    fitnessViewModel
                )
            }
        }
    )
}

/**
 * Composable function for the content of the Saved Exercise screen. It displays the saved exercises,
 * allowing filtering and searching.
 * @param exercises is the same as before,
 * @param searchQuery is the same as before,
 * @param isFavouritesOnly is the same as before,
 */
@Composable
fun SavedExerciseContent(
    exercises: List<Exercise>,
    searchQuery: String,
    isFavouritesOnly: Boolean,
    onDelete: (String) -> Unit,
    fitnessViewModel: FitnessViewModel
) {
    // Filtering exercises based on search and favorites criteria
    val filteredExercises = exercises.filter { exercise ->
        exercise.name.contains(searchQuery, ignoreCase = true) &&
                (!isFavouritesOnly || exercise.isFavourite)
    }

    // Lazy column for displaying the filtered exercises
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(filteredExercises) { exercise ->
            // Displaying each exercise as a card
            ExerciseCard(exercise, onDelete, onFavouriteToggle = { exerciseName ->
                val updatedExercises = exercises.map {
                    if (it.name == exerciseName) {
                        it.copy(isFavourite = !it.isFavourite)
                    } else {
                        it
                    }
                }
                fitnessViewModel.updateExercisesFile(updatedExercises)
            })
        }
    }
}

/**
 * Composable function for the filter button, allowing the user to filter exercises by favorites.
 * @param onToggleFilterDropdown updates whether the filter options are chosen or not
 * @param expanded is for the dropdown menu, is it closed or open
 * @param onFavouritesFilterChanged updates the filter
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
 * Composable function for the Exercise Card, displaying details for each saved exercise.
 * @param exercise is the same as before,
 */
@Composable
fun ExerciseCard(
    exercise: Exercise,
    onDelete: (String) -> Unit,
    onFavouriteToggle: (String) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    // Card displaying each exercise
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { showDialog.value = true },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
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
            // Details for the card (name, sets, reps, weight)
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Sets: ${exercise.sets} | Reps: ${exercise.reps} | Weight: ${exercise.weight} kg",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            // Favourites handling
            Spacer(modifier = Modifier.weight(1f))
            var isFavorite by remember { mutableStateOf(exercise.isFavourite) }
            IconButton(
                onClick = {
                    // Toggle the favorite status when the button is clicked
                    isFavorite = !isFavorite

                    // Call onFavouriteToggle with the updated favorite status
                    onFavouriteToggle(exercise.name)
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = "Favourite"
                )
            }
        }
        // Details dialog for each exercise
        ExerciseCardExpandedDetails(showDialog, exercise, onDelete)
    }
}

/**
 * Composable function for the expanded details dialog of an Exercise Card.
 * @param showDialog indicates whether the details dialog is shown
 * @param exercise is the same as before,
 */
@Composable
private fun ExerciseCardExpandedDetails(
    showDialog: MutableState<Boolean>,
    exercise: Exercise,
    onDelete: (String) -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            text = {
                // Exercise details in the dialog
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
                    // Close button
                    TextButton(onClick = {
                        showDialog.value = false
                    }) {
                        Text("Close")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    // Delete button
                    TextButton(onClick = {
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