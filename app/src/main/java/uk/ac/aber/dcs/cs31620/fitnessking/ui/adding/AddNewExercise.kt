package uk.ac.aber.dcs.cs31620.fitnessking.ui.adding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.AddExerciseTopAppBar
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.util.AddNewImage
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import uk.ac.aber.dcs.cs31620.fitnessking.ui.util.OutlinedTextFieldWithKeyboardDismiss

/**
 * This file is the screen where the user is able to add new exercises to the exercise database,
 * it will enforce a numbers only for sets, weights, and reps. It will also allow the user to select
 * an image and type a name in for the exercise. Once the button is pressed, the program will then
 * add the exercise into the database if it meets the requirements, ideally, all boxes should be filled
 * in before doing this.
 *
 * @author Lauren Davis
 * @date 16/1/2024
 * @version 1.4
 */

/**
 * Top level function which calls the required information to pass through to the screen itself.
 */
@Composable
fun AddNewExerciseTopLevel(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel = viewModel(),
){
    AddNewExercise(
        navController = navController,
        insertExercise = { exercise ->
            exerciseViewModel.insertExercise(exercise) // Return result directly
        }
    )
}

/**
 * This function is the screen itself, it has a scaffold of all the required information for the exercises
 * which is then passed through each individual box for the user to update.
 */
@Composable
fun AddNewExercise(
    navController: NavController,
    insertExercise: (ExerciseEntity) -> Boolean
) {
    //Initialisation of the variables
    var name by remember { mutableStateOf("") }
    var sets by remember { mutableIntStateOf(0) }
    var reps by remember { mutableIntStateOf(0) }
    var weight by remember { mutableIntStateOf(0) }
    var dropset by remember { mutableStateOf(false) }
    var image by remember { mutableStateOf("") }

    //This is for the control of the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //Scaffold for the screen itself
    Scaffold(
        topBar = {
            AddExerciseTopAppBar(navController) //This app bar is different to the main one, it will allow the user to go back on themselves
        },
        content = { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .clickable{keyboardController?.hide()}
            ) {
                AddNewImageBox(
                    image = image,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    updateImagePath = { newImagePath ->
                        image = newImagePath
                    }
                )
                ExerciseNameInput(
                    name = name,
                    modifier = Modifier,
                    updateName = {
                        name = it
                    }
                )
                SetsInput(
                    sets = sets,
                    modifier = Modifier,
                    updateSet = {
                        sets = it
                    }
                )
                RepsInput(
                    reps = reps,
                    modifier = Modifier,
                    updateReps = {
                        reps = it
                    }
                )
                WeightInput(
                    weight = weight,
                    modifier = Modifier,
                    updateWeight = {
                        weight = it
                    }
                )
                DropsetButton(
                    dropset = false,
                    updateDropset = {
                        dropset = it
                    }
                )
                AddExerciseButton(
                    name = name,
                    sets = sets,
                    reps = reps,
                    weight = weight,
                    dropset = dropset,
                    image = image,
                    doInsert = { newExercise ->
                        insertExercise(newExercise)
                    },
                    navController
                )

            }
        },
    )
}

/**
 * This is where the user will input the name of the exercise.
 * @return the updated name for the exercise
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
 * This is where the user is able to input the number of sets they want to do.
 * @return the updated number of sets
 */
@Composable
fun SetsInput(
    sets: Int,
    modifier: Modifier,
    updateSet: (Int) -> Unit
){
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
 * This is the dropset tick box, where the user selects if the exercise is a dropset or not
 * @return the updated boolean value for dropsets
 */
@Composable
fun DropsetButton(
    dropset: Boolean,
    updateDropset: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = dropset,
            onCheckedChange = updateDropset
        )
        Text(
            text = "Dropset",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
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
            .height(220.dp)
            .padding(16.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
    ) {
        AddNewImage(
            imagePath = image,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
                //.padding(16.dp),
            updateImagePath = updateImagePath
        )
    }
}

/**
 * This is the button where the user submits their chosen exercise data, it will then push it to the
 * database
 */
@Composable
fun AddExerciseButton(
    name: String,
    sets: Int,
    reps: Int,
    weight: Int,
    dropset: Boolean,
    image: String,
    doInsert: (ExerciseEntity) -> Boolean,
    navController: NavController
) {
    var showSnackbar by remember { mutableStateOf(false) }
    var isExerciseAdded by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (name.isNotEmpty() && image.isNotEmpty()) {
                val exerciseEntity = ExerciseEntity(
                    exerciseId = 0,
                    name = name,
                    sets = sets,
                    reps = reps,
                    weight = weight,
                    isDropSet = dropset,
                    image = image
                )
                val exerciseAdded = doInsert(exerciseEntity)
                isExerciseAdded = exerciseAdded
                showSnackbar = true

                if (exerciseAdded) {
                    navController.navigateUp()  // Navigate up on success
                }
            } else {
                // Highlight input fields with errors will be added as flare, if time
            }
        },
        content = {
            if (isExerciseAdded) {
                Text(text = "Exercise Added!")
            } else {
                Text(text = "Add Exercise")
            }
        }
    )
}

/**
 * This is just the preview of the screen
 */
@Preview
@Composable
private fun AddNewWorkoutPreview(){
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        AddNewExerciseTopLevel(navController)
    }
}