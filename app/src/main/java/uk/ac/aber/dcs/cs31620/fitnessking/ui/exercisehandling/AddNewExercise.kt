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
 * This file is the screen where the user is able to add new exercises to the exercise database,
 * it will enforce a numbers only for sets, weights, and reps. It will also allow the user to select
 * an image and type a name in for the exercise. Once the button is pressed, the program will then
 * add the exercise into the csv if it meets the requirements, ideally, all boxes should be filled
 * in before doing this.
 */

/**
 * Top level function which calls the required information to pass through to the screen itself.
 */
@Composable
fun AddNewExerciseTopLevel(
    navController: NavHostController,
){
    AddNewExercise(
        navController = navController
    )
}

/**
 * This function is the screen itself, it has a scaffold of all the required information for the exercises
 * which is then passed through each individual box for the user to update.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNewExercise(
    navController: NavController
) {
    //Initialisation of the variables
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
    var focus by remember { mutableStateOf(focusValues[0])}

    val fitnessViewModel: FitnessViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    //This is for the control of the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    // This is for the toast message
    var areFieldsValid by remember { mutableStateOf(true) }

    //Scaffold for the screen itself
    Scaffold(
        topBar = {
            //This app bar is different to the main one, it will allow the user to go back on themselves
            SmallTopAppBar(navController, title = stringResource(id = R.string.addExercise)) 
        },
        // Floating action button for the addition of the exercise
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (name.isNotEmpty() && sets > 0 && reps > 0 && image.isNotEmpty() && focus.isNotEmpty() && lengthOfExercise > 0 && rest > 0) {
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
                    .clickable { keyboardController?.hide() } ,
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
 * This is where the user will input the name of the exercise.
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
        onValueChange = {updateName(it)},
        modifier = modifier
    )
}

/**
 * Function to add what the focus of the exercise is
 * @return focus of exercise
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
 * This is where the user is able to input the number of sets they want to do.
 * @return the updated number of sets
 */
@Composable
fun SetsInput(
    sets: Int,
    modifier: Modifier,
    updateSet: (Int) -> Unit
){
    // This is the outlined text field which dismisses the keyboard when pressed outside the area, or pressing enter,
    // it is used throughout this screen
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
        // This forces it to only show the number typing
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.sets)) },
        modifier = modifier
    )
}


/**
 * This is where the user inputs the number of reps they want to do
 * @return the updated number of reps
 */
@Composable
fun RepsInput(
    reps: Int,
    modifier: Modifier,
    updateReps: (Int) -> Unit
){
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
 * This is where the user inputs the weight they want to use during the exercise
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
 * This is the length input function, it allows the user to determine their own approximate times
 * for their exercise
 * @return the approximate length of their exercise
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
 * This is the dropset tick box, where the user selects if the exercise is a dropset or not
 * @return the updated boolean value for dropsets
 */
@Composable
fun DropsetButton(
    dropset: Boolean,
    updateDropset: (Boolean) -> Unit
) {
    var checkedState by remember { mutableStateOf(dropset) } //Managed internally because the attempts before, did not work.
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
 * This allows the user to determine their rest time in-between sets
 * @return the rest time chosen 
 */
@Composable
fun RestTime(
    rest: Int,
    onRestChange: (Int) -> Unit,
    modifier: Modifier
) {
    var restTime by remember { mutableIntStateOf(rest) }

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
 * This is the AddNewImageBox where the box holds the option to add a new image and will call the
 * function "AddNewImage" in the util package.
 * @return image path of the file chosen
 */

@Composable
fun AddNewImageBox(
    image: String,
    modifier: Modifier,
    updateImagePath: (String) -> Unit
) {
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
 * This is the preview for the screen
 */
@Preview
@Composable
private fun AddNewExercisePreview() {
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        AddNewExerciseTopLevel(navController = navController)
    }
}