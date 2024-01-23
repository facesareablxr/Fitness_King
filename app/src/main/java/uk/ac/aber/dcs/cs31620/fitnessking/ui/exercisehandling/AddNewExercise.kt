package uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.datafiles.FitnessViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.ButtonSpinner
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.SmallTopAppBar
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.util.AddNewImage
import uk.ac.aber.dcs.cs31620.fitnessking.ui.util.OutlinedTextFieldWithKeyboardDismiss

/**
 * Top-level composable function that initializes the required information and passes it to the screen.
 *
 * @param navController is the navigation controller for managing navigation within the app.
 */
@Composable
fun AddNewExerciseTopLevel(
    navController: NavHostController,
) {
    AddNewExercise(
        navController = navController
    )
}

/**
 * Composable function representing the Add Exercise screen. Users can input various details about
 * their exercise, and upon pressing the floating action button, the information is added to a CSV file.
 *
 * @param navController is the navigation controller for managing navigation within the app.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNewExercise(
    navController: NavController
) {
    // Initialization of variables
    val values = stringArrayResource(id = R.array.Focus)
    val focusValues = values.copyOfRange(1, values.size)
    var name by remember { mutableStateOf("") }
    var sets by remember { mutableIntStateOf(0) }
    var reps by remember { mutableIntStateOf(0) }
    var weight by remember { mutableIntStateOf(0) }
    var dropset by remember { mutableStateOf(false) }
    var lengthOfExercise by remember { mutableIntStateOf(0) }
    var image by remember { mutableStateOf("") }
    val favourite by remember { mutableStateOf(false) }
    var rest by remember { mutableIntStateOf(0) }
    var focus by remember { mutableStateOf(focusValues[0]) }

    val fitnessViewModel: FitnessViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    // Keyboard control and toast message
    val keyboardController = LocalSoftwareKeyboardController.current
    var areFieldsValid by remember { mutableStateOf(true) }

    // Scaffold for the screen
    Scaffold(
        topBar = {
            // Custom app bar allowing the user to navigate back
            SmallTopAppBar(navController, title = stringResource(id = R.string.addExercise))
        },
        // Floating action button for adding the exercise
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (name.isNotEmpty() && sets > 0 && reps > 0 && image.isNotEmpty() &&
                        focus.isNotEmpty() && lengthOfExercise > 0 && rest > 0
                    ) {
                        fitnessViewModel.addExercise(
                            name = name,
                            sets = sets,
                            reps = reps,
                            weight = weight,
                            dropset = dropset,
                            lengthOfExercise = lengthOfExercise,
                            image = image,
                            favourite = favourite,
                            rest = rest,
                            focus = focus
                        )
                        navController.navigateUp()
                    } else {
                        areFieldsValid = false
                        coroutineScope.launch {
                            SnackbarHostState().showSnackbar(
                                message = "Please fill in all required fields",
                                actionLabel = "Dismiss",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            )
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.addExercise)
                )
            }
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .clickable { keyboardController?.hide() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Add new image box
                item {
                    AddNewImageBox(
                        image = image,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        updateImagePath = { newImagePath ->
                            image = newImagePath
                        }
                    )
                }
                // Exercise name input box
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    ExerciseNameInput(
                        name = name,
                        modifier = Modifier,
                        updateName = {
                            name = it
                            areFieldsValid = true
                        }
                    )
                }
                // Exercise focus drop down box
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    ExerciseFocus(
                        values = focusValues,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .wrapContentWidth(),
                        updateFocus = {
                            focus = it
                            areFieldsValid = true
                        }
                    )
                }
                // Sets input box
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    SetsInput(
                        sets = sets,
                        modifier = Modifier,
                        updateSet = {
                            sets = it
                            areFieldsValid = true
                        }
                    )
                }
                // Reps input box
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    RepsInput(
                        reps = reps,
                        modifier = Modifier,
                        updateReps = {
                            reps = it
                            areFieldsValid = true
                        }
                    )
                }
                // Weight input box
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    WeightInput(
                        weight = weight,
                        modifier = Modifier,
                        updateWeight = {
                            weight = it
                            areFieldsValid = true
                        }
                    )
                }
                // Length input box
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    LengthInput(
                        length = lengthOfExercise,
                        updateLength = {
                            lengthOfExercise = it
                            areFieldsValid = true
                        }
                    )
                }
                // Dropset tick box
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    DropsetButton(
                        dropset = false,
                        updateDropset = {
                            dropset = it
                            areFieldsValid = true
                        }
                    )
                }
                // Rest time slider
                item {
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "Rest time:")
                    RestTime(
                        modifier = Modifier,
                        rest = 0,
                        onRestChange = {
                            rest = it
                            areFieldsValid = true
                        }
                    )
                    Spacer(modifier = Modifier.padding(30.dp))
                }
            }
            SnackbarHost(
                hostState = remember { SnackbarHostState() },
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}

/**
 * Composable function for the exercise name input.
 * @return the name for the exercise
 */
@Composable
fun ExerciseNameInput(
    name: String,
    modifier: Modifier,
    updateName: (String) -> Unit
) {
    OutlinedTextFieldWithKeyboardDismiss(
        value = name,
        label = {
            Text(text = stringResource(id = R.string.addExerciseName))
        },
        onValueChange = { updateName(it) },
        modifier = modifier
    )
}

/**
 * Composable function to select the focus of the exercise.
 * @return the focus of the exercise
 */
@Composable
fun ExerciseFocus(
    values: Array<String>,
    modifier: Modifier,
    updateFocus: (String) -> Unit
) {
    ButtonSpinner(
        items = values.asList(),
        modifier = modifier,
        itemClick = {
            updateFocus(it)
        },
        label = "Any Focus"
    )
}

/**
 * Composable function for inputting the number of sets.
 * @return the updated number of sets
 */
@Composable
fun SetsInput(
    sets: Int,
    modifier: Modifier,
    updateSet: (Int) -> Unit
) {
    // Outlined text field for numeric input
    OutlinedTextFieldWithKeyboardDismiss(
        value = sets.toString(),
        onValueChange = { newValue ->
            val newSets = try {
                newValue.toInt()
            } catch (e: NumberFormatException) {
                return@OutlinedTextFieldWithKeyboardDismiss
            }
            updateSet(newSets)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.sets)) },
        modifier = modifier
    )
}

/**
 * Composable function for inputting the number of reps.
 * @return the updated number of reps
 */
@Composable
fun RepsInput(
    reps: Int,
    modifier: Modifier,
    updateReps: (Int) -> Unit
) {
    OutlinedTextFieldWithKeyboardDismiss(
        value = reps.toString(),
        onValueChange = { newValue ->
            val newReps = try {
                newValue.toInt()
            } catch (e: NumberFormatException) {
                return@OutlinedTextFieldWithKeyboardDismiss
            }
            updateReps(newReps)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.reps)) },
        modifier = modifier
    )
}

/**
 * Composable function for inputting the weight to be used during the exercise.
 * @return the updated weight in kg
 */
@Composable
fun WeightInput(
    weight: Int,
    modifier: Modifier = Modifier,
    updateWeight: (Int) -> Unit
) {
    OutlinedTextFieldWithKeyboardDismiss(
        value = weight.toString(),
        onValueChange = { newValue ->
            val newWeight = try {
                newValue.toInt()
            } catch (e: NumberFormatException) {
                return@OutlinedTextFieldWithKeyboardDismiss
            }
            updateWeight(newWeight)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.weight)) },
        modifier = modifier
    )
}

/**
 * Composable function for inputting the approximate length of the exercise.
 * @return the approximate length of the exercise
 */
@Composable
fun LengthInput(
    length: Int,
    updateLength: (Int) -> Unit
) {
    OutlinedTextFieldWithKeyboardDismiss(
        value = length.toString(),
        onValueChange = { newValue ->
            val newLength = try {
                newValue.toInt()
            } catch (e: NumberFormatException) {
                return@OutlinedTextFieldWithKeyboardDismiss
            }
            updateLength(newLength)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.length)) },
        modifier = Modifier
    )
}

/**
 * Composable function for the dropset tick box.
 * @return the updated boolean value for dropsets
 */
@Composable
fun DropsetButton(
    dropset: Boolean,
    updateDropset: (Boolean) -> Unit
) {
    var checkedState by remember { mutableStateOf(dropset) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            checkedState = !checkedState
            updateDropset(checkedState)
        }
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { newCheckedValue ->
                checkedState = newCheckedValue
            }
        )
        Text(
            text = "Dropset",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * Composable function for selecting the rest time between sets.
 * @return the selected rest time
 */
@Composable
fun RestTime(
    rest: Int,
    onRestChange: (Int) -> Unit,
    modifier: Modifier
) {
    var restTime by remember { mutableIntStateOf(rest) }

    // Slider for rest time selection
    Slider(
        value = restTime.toFloat(),
        onValueChange = { newValue ->
            restTime = newValue.toInt()
            onRestChange(restTime)
        },
        valueRange = 0f..30f,
        steps = 31,
        onValueChangeFinished = {},
        modifier = modifier.width(350.dp)
    )
    Text(
        text = "$restTime minutes",
        modifier = Modifier.padding(start = 16.dp)
    )
}

/**
 * Composable function for the Add New Image Box, allowing users to add a new image.
 * @return the image path of the chosen file
 */
@Composable
fun AddNewImageBox(
    image: String,
    modifier: Modifier,
    updateImagePath: (String) -> Unit
) {
    // Box for displaying the image and calling the AddNewImage function
    Box(
        modifier = modifier
            .height(300.dp)
            .width(150.dp)
            .padding(16.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable {}
    ) {
        AddNewImage(
            imagePath = image,
            modifier = Modifier
                .fillMaxWidth(),
            updateImagePath = updateImagePath
        )
    }
}

/**
 * Preview function
 */
@Preview
@Composable
private fun AddNewExercisePreview() {
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        AddNewExerciseTopLevel(navController = navController)
    }
}