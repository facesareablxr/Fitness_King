package uk.ac.aber.dcs.cs31620.fitnessking.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.Exercise
import uk.ac.aber.dcs.cs31620.fitnessking.model.datafiles.FitnessViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen
import java.time.LocalDate

/**
 * This is the home screen top level, it forms the
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: FitnessViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        pageContent = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                HomeScreenContent(
                    modifier = Modifier.padding(8.dp),
                    viewModel = viewModel,
                    navController
                )
            }
        }
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    viewModel: FitnessViewModel,
    navController: NavHostController
) {
    var todaysExercises by remember {
        mutableStateOf<List<Exercise>?>(null)
    }
    var todaysWorkout by remember {
        mutableStateOf<Pair<String?, String?>?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        try {
            val currentDate = LocalDate.now()
            todaysExercises = viewModel.getTodaysExercisesWithDetails(currentDate)
            todaysWorkout = viewModel.getTodaysWorkoutWithDetails(currentDate)
        } catch (e: Exception) {
            throw NoSuchElementException("Workout not found.")
        }
    }

    LazyColumn(
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            val currentDayCaps = LocalDate.now().dayOfWeek.name
            val currentDay = currentDayCaps.lowercase().replaceFirstChar { it.uppercase() }
            Text(
                text = currentDay,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        todaysWorkout?.let { workout ->
            todaysExercises?.let { exercises ->
                item {
                    WorkoutOverviewCard(
                        modifier = modifier,
                        workoutWithExercises = workout,
                        exercises = exercises,
                        fitnessViewModel = viewModel,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            todaysExercises?.let { exercises ->
                items(exercises) { exercise ->
                    ExerciseCard(exercise = exercise, modifier = modifier)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } ?: run {
                item {
                    Text(
                        text = stringResource(R.string.noWorkout),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Button(
                    onClick = {
                        navController.navigate(Screen.AddWorkout.route)
                    },
                ) {
                    Text("Add Workout")
                }
            }
        }
    }
}

@Composable
fun WorkoutOverviewCard(
    modifier: Modifier,
    workoutWithExercises: Pair<String?, String?>,
    exercises: List<Exercise>,
    fitnessViewModel: FitnessViewModel = viewModel()
) {
    val (day, focus) = workoutWithExercises
    var isMenuVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // ElevatedCard for the workout details
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Workout Overview",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box {
                        // Three dot menu
                        IconButton(
                            onClick = { isMenuVisible = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = isMenuVisible,
                            onDismissRequest = { isMenuVisible = false },
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(8.dp)
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    isMenuVisible = false
                                    fitnessViewModel.deleteWorkoutWithExercises(day)
                                }
                            ) {
                                Text("Delete Workout")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Focus:")
                        }
                        append("$focus")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Number of Exercises: ")
                        }
                        append("${exercises.size}")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                val totalLength = exercises.sumOf { it.length + (it.restTime * it.sets)}
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Approximate Length: ")
                        }
                        append("$totalLength minutes")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp)) // Reduce the space here
            }
        }
    }
}


/**
 *
 */
@Composable
fun ExerciseCard(
    exercise: Exercise,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(model = exercise.image)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = modifier.height(4.dp))

            // This is the exercise image that is stored with it in the text file
            Image(
                painter = painter,
                contentDescription = "Exercise Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Spacer(modifier = modifier.height(4.dp))

            // Display full exercise information with rest time
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Sets:")
                        }
                        append(" ${exercise.sets}")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Reps:")
                        }
                        append(" ${exercise.reps}")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Weight:")
                        }
                        append(" ${exercise.weight} kg")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Rest Time:")
                        }
                        append(" ${exercise.restTime} minutes")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}