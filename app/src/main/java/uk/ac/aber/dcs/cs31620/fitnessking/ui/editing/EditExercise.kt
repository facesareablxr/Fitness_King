package uk.ac.aber.dcs.cs31620.fitnessking.ui.editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.adding.AddNewImageBox
import uk.ac.aber.dcs.cs31620.fitnessking.ui.adding.DropsetButton
import uk.ac.aber.dcs.cs31620.fitnessking.ui.adding.ExerciseNameInput
import uk.ac.aber.dcs.cs31620.fitnessking.ui.adding.RepsInput
import uk.ac.aber.dcs.cs31620.fitnessking.ui.adding.SetsInput
import uk.ac.aber.dcs.cs31620.fitnessking.ui.adding.WeightInput
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.AddExerciseTopAppBar

@Composable
fun EditExerciseTopLevel(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel = viewModel(),
    exerciseId: Int
) {
    val exercise = exerciseViewModel.getExerciseById(exerciseId)

    EditExercise(
        navController = navController,
        updateExercise = { exercise ->
            exerciseViewModel.insertExercise(exercise)
        },
        exercise = exercise
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditExercise(
    navController: NavController,
    exercise: ExerciseEntity,
    updateExercise: (ExerciseEntity) -> Boolean
) {
    //Initialisation of variables
    var name by remember { mutableStateOf(exercise.name) }
    var sets by remember { mutableIntStateOf(exercise.sets) }
    var reps by remember { mutableIntStateOf(exercise.reps) }
    var weight by remember { mutableIntStateOf(exercise.weight) }
    var dropset by remember { mutableStateOf(exercise.isDropSet) }
    var image by remember { mutableStateOf(exercise.image) }

    //This is for the control of the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

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
                EditExerciseButton(
                    name = name,
                    sets = sets,
                    reps = reps,
                    weight = weight,
                    dropset = dropset,
                    image = image,
                    doUpdate = { newExercise ->
                        updateExercise(newExercise)
                    },
                    navController
                )

            }
        },
    )
}

/**
 * This is the button where the user submits their chosen exercise data, it will then push it to the
 * database
 */
@Composable
fun EditExerciseButton(
    name: String,
    sets: Int,
    reps: Int,
    weight: Int,
    dropset: Boolean,
    image: String,
    doUpdate: (ExerciseEntity) -> Boolean,
    navController: NavController
) {
    var showSnackbar by remember { mutableStateOf(false) }
    var isExerciseChanged by remember { mutableStateOf(false) }

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
                val exerciseChanged = doUpdate(exerciseEntity)
                isExerciseChanged = exerciseChanged
                showSnackbar = true

                if (exerciseChanged) {
                    navController.navigateUp()  // Navigate up on success
                }
            } else {
                // Highlight input fields with errors will be added as flare, if time
            }
        },
        content = {
            if (isExerciseChanged) {
                    Text(text = "Exercise Edited!")
            } else {
                Text(text = "Edit Exercise")
            }
        }
    )
}
