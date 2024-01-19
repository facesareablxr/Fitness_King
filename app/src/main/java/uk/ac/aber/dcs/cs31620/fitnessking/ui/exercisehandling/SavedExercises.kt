package uk.ac.aber.dcs.cs31620.fitnessking.ui.exercisehandling

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen

/**
 *
 */

/**
 *
 */
@Composable
fun SavedExerciseTopLevel(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel = viewModel()
) {
    val exerciseList by exerciseViewModel.allExercises.observeAsState(listOf())

    SavedExerciseScreen(
        navController = navController,
        exerciseList = exerciseList
    )
}


/**
 *
 */
@Composable
fun SavedExerciseScreen(
    navController: NavHostController,
    exerciseList: List<ExerciseEntity>
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    TopLevelScaffold(
        coroutineScope = coroutineScope,
        navController = navController,
        floatingActionButton = {
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

                SavedExerciseContent(
                    exerciseList = exerciseList,
                    navController = navController
                )
            }
        }
    )
}

/**
 *
 */
@Composable
fun SavedExerciseContent(
    exerciseList: List<ExerciseEntity>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(exerciseList) { exercise ->
            ExerciseCard(exercise = exercise, navController = navController)
        }
    }
}

/**
 *
 */
@Composable
fun ExerciseCard(
    exercise: ExerciseEntity,
    navController: NavHostController
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
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = exercise.image),
                contentDescription = exercise.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.small)

            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }

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
                            navController.navigate(Screen.EditExercise.route)
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