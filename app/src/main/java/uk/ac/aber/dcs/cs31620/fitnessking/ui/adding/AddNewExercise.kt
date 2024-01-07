package uk.ac.aber.dcs.cs31620.fitnessking.ui.adding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.entity.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.MainPageNavigationBar

@Composable
fun AddNewExercise(
    navController: NavController,
    insertExercise: (String, Int, Int, Int, String, Boolean, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var sets by remember { mutableIntStateOf(0) }
    var reps by remember { mutableIntStateOf(0) }
    var weight by remember { mutableIntStateOf(0) }
    var dropset by remember { mutableStateOf(false) }
    var image by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            MainPageNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
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
        }
    }
}

fun AddExercise(
    name: String,
    sets: Int,
    reps: Int,
    weight: Int,
    length: Int,
    dropset: Boolean,
    image: String,
    doInsert: (ExerciseEntity) -> Unit = {}
) {
    if (name.isNotEmpty() && image.isNotEmpty()){
        val exercise = ExerciseEntity(
            exerciseId = 0,
            name = name,
            sets = sets,
            reps = reps,
            weight = weight,
            length = length,
            isDropSet = dropset,
            imageUri = image
        ) //doInsert (exercise)
    }
}

@Composable
fun ExerciseNameInput(
    name: String,
    modifier: Modifier,
    updateName: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        label = {
            Text(text = stringResource(id = R.string.addExerciseName))
        },
        onValueChange = {updateName(it)},
        modifier = modifier
    )
}

@Composable
fun SetsInput(
    sets: Int,
    modifier: Modifier,
    updateSet: (Int) -> Unit
){
    OutlinedTextField(
        value = sets.toString(),
        onValueChange = { newValue ->
            val newSets = try {
                newValue.toInt()
            } catch (e: NumberFormatException) {
                return@OutlinedTextField
            }
            updateSet(newSets)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.sets)) },
        modifier = modifier
    )
}

@Composable
fun RepsInput(
    reps: Int,
    modifier: Modifier,
    updateReps: (Int) -> Unit
){
    OutlinedTextField(
        value = reps.toString(),
        onValueChange = { newValue ->
            val newReps = try {
                newValue.toInt()
            } catch (e: NumberFormatException) {
                return@OutlinedTextField
            }
            updateReps(newReps)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.reps)) },
        modifier = modifier
    )
}

@Composable
fun WeightInput(
    weight: Int,
    modifier: Modifier = Modifier,
    updateWeight: (Int) -> Unit
) {
    OutlinedTextField(
        value = weight.toString(),
        onValueChange = { newValue ->
            val newWeight = try {
                newValue.toInt()
            } catch (e: NumberFormatException) {
                return@OutlinedTextField
            }
            updateWeight(newWeight)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(id = R.string.weight)) },
        modifier = modifier
    )
}

