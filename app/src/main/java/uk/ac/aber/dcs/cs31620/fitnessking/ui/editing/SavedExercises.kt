package uk.ac.aber.dcs.cs31620.fitnessking.ui.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseEntity
import uk.ac.aber.dcs.cs31620.fitnessking.model.database.exercise.ExerciseViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.schedule.ExerciseImage

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
            Column(modifier = Modifier) {
                SavedExerciseContent(exerciseList = exerciseList)
            }

        }
    )
}

@Composable
fun SavedExerciseContent(exerciseList: List<ExerciseEntity>) {
    LazyColumn {
        items(exerciseList) { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun ExerciseCard(exercise: ExerciseEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
           // ExerciseImage(imageRes = exercise.image)
            Text(text = exercise.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sets: ${exercise.sets}, Reps: ${exercise.reps}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Weight: ${exercise.weight}")
            if (exercise.isDropSet) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Drop Set", color = Color.Red)
            }


        }
    }
}


@Preview
@Composable
private fun SavedExerciseScreen() {
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        SavedExerciseTopLevel(navController = navController)
    }
}

