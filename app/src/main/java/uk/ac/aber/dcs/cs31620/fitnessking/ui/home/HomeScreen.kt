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
import uk.ac.aber.dcs.cs31620.fitnessking.model.dataclasses.WorkoutWithExercises
import uk.ac.aber.dcs.cs31620.fitnessking.model.datafiles.FitnessViewModel
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.navigation.Screen
import java.time.LocalDate

/**
 * Top-level composable function for the Home Screen, providing the main entry point.
 * @param navController is the NavHostController for navigation
 * @param viewModel is the FitnessViewModel for accessing workout data
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: FitnessViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    // Display the top-level scaffold for the Home Screen
    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        pageContent = { innerPadding ->
            // Surface for the page content
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Display the content of the Home Screen
                HomeScreenContent(
                    modifier = Modifier.padding(8.dp),
                    viewModel = viewModel,
                    navController
                )
            }
        }
    )
}

/**
 * Represents the content of the Home Screen, displaying today's workout and exercises.
 * @param modifier is the Modifier for styling
 * @param viewModel is the FitnessViewModel for accessing workout data
 * @param navController is the NavHostController for navigation
 */
@Composable
fun HomeScreenContent(
    modifier: Modifier,
    viewModel: FitnessViewModel,
    navController: NavHostController,
) {
    // State variables for today's exercises and workout
    var todaysExercises by remember {
        mutableStateOf<List<Exercise>?>(null)
    }
    var todaysWorkout by remember {
        mutableStateOf<Pair<String?, String?>?>(null)
    }

    var todaysFullWorkout by remember{
        mutableStateOf<List<WorkoutWithExercises>?>(null)
    }

    // Fetch today's exercises and workout asynchronously
    LaunchedEffect(key1 = Unit) {
        try {
            val currentDate = LocalDate.now()
            todaysExercises = viewModel.getTodaysExercisesWithDetails(currentDate)
            todaysWorkout = viewModel.getTodaysWorkoutWithDetails(currentDate)
            todaysFullWorkout = viewModel.getTodaysWorkout(currentDate)
        } catch (e: Exception) {
            throw NoSuchElementException("Workout not found.")
        }
    }

    // LazyColumn for displaying content
    LazyColumn(
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Display the greeting with the username
            val greeting = "${stringResource(R.string.greeting)} ${stringResource(R.string.name)}"

            Text(
                text = greeting,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Display "Today's Session" underneath
            Text(
                text = "Today's Session",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Check if there's a workout for today
        todaysWorkout?.let { workout ->
            todaysExercises?.let { exercises ->
                item {
                    // Display the workout overview card
                    todaysFullWorkout?.let {
                        WorkoutOverviewCard(
                            modifier = modifier,
                            workoutWithExercises = workout,
                            exercises = exercises,
                            fitnessViewModel = viewModel,
                            todaysWorkout = it
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            todaysExercises?.let { exercises ->
                items(exercises) { exercise ->
                    // Display each exercise card
                    ExerciseCard(exercise = exercise, modifier = modifier)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } ?: run {
                item {
                    // Display a message when no workout is found
                    Text(
                        text = stringResource(R.string.noWorkout),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                // Button to navigate to the Add Workout screen
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

/**
 * Represents a stylized workout overview card that displays workout details.
 * @param modifier is the Modifier for styling
 * @param workoutWithExercises is the Pair of day and focus for the workout
 * @param exercises is the list of exercises for the workout
 * @param fitnessViewModel is the FitnessViewModel for accessing workout data
 */
@Composable
fun WorkoutOverviewCard(
    modifier: Modifier,
    workoutWithExercises: Pair<String?, String?>,
    exercises: List<Exercise>,
    fitnessViewModel: FitnessViewModel = viewModel(),
    todaysWorkout: List<WorkoutWithExercises>
) {
    val (day, focus) = workoutWithExercises
    var isMenuVisible by remember { mutableStateOf(false) }

    // Box for positioning elements
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // ElevatedCard for the workout details
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Column for organizing workout details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Row for workout day and three-dot button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Display the workout overview title
                    Text(
                        text = "Workout Overview",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    // Box for three-dot menu
                    Box {
                        // Three dot menu button
                        IconButton(
                            onClick = { isMenuVisible = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        // Dropdown menu for workout options
                        DropdownMenu(
                            expanded = isMenuVisible,
                            onDismissRequest = { isMenuVisible = false },
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(8.dp)
                        ) {
                            // Dropdown menu item for deleting the workout
                            DropdownMenuItem(
                                onClick = {
                                    isMenuVisible = false
                                    fitnessViewModel.updateWorkoutsWithExercisesFile(todaysWorkout)
                                }
                            ) {
                                Text("Delete Workout")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Display workout focus
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Focus:")
                        }
                        append("$focus")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                // Display number of exercises
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Number of Exercises: ")
                        }
                        append("${exercises.size}")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                // Display approximate workout length
                val totalLength = exercises.sumOf { it.length + (it.restTime * it.sets) }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Approximate Length: ")
                        }
                        append("$totalLength minutes")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

/**
 * Represents a stylized exercise card that displays exercise details.
 * @param exercise is the Exercise object to display
 * @param modifier is the Modifier for styling
 */
@Composable
fun ExerciseCard(
    exercise: Exercise,
    modifier: Modifier,
) {
    // Async image painter for exercise image
    val painter = rememberAsyncImagePainter(model = exercise.image)

    // Card for displaying exercise details
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Column for organizing exercise details
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Display exercise name
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = modifier.height(4.dp))

            // Display exercise image
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

            // Display exercise details with sets, reps, weight, and rest time
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