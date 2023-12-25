package uk.ac.aber.dcs.cs31620.fitnessking.ui.workouts

import SmallTopAppBar
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.aber.dcs.cs31620.fitnessking.R

@Composable
fun EditSavedWorkouts(
    onAddExercise: (String, Int, Int, Int, String, Boolean, String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var focus by remember { mutableStateOf("Chest") }
    var dropset by remember { mutableStateOf(false) }
    var image by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmallTopAppBar()

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(id = R.string.exercise_name)) }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(id = R.string.sets)) }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(id = R.string.reps)) }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(id = R.string.weight)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        FocusDropdown { selectedFocus -> focus = selectedFocus }
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Checkbox(
                checked = dropset,
                onCheckedChange = { dropset = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(id = R.string.dropset))
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = image,
            onValueChange = { image = it },
            label = { Text(stringResource(id = R.string.image_url)) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                validateAndAddExercise(name, sets, reps, weight, focus, dropset, image, onAddExercise)
            }
        ) {
            Text(stringResource(id = R.string.add_exercise_button))
        }
    }
}

fun validateAndAddExercise(
    name: TextFieldValue,
    sets: String,
    reps: String,
    weight: String,
    focus: String,
    dropset: Boolean,
    image: TextFieldValue,
    onAddExercise: Any
) {
    TODO("Not yet implemented")
}

@Composable
fun FocusDropdown(onFocusSelected: (String) -> Unit) {
    val focusOptions = listOf("Chest", "Back", "Legs", "Arms", "Shoulders", "Core")
    var expanded by remember { mutableStateOf(false) }
    var selectedFocus by remember { mutableStateOf(focusOptions[0]) }

    TextField(
        value = selectedFocus,
        onValueChange = { }, // Disable text editing
        readOnly = true,
        label = { Text(stringResource(id = R.string.focus)) },
        trailingIcon = {
            IconButton({ expanded = true }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.nav_drawer_menu)
                )
            }
        }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        focusOptions.forEach {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
   // EditSavedWorkouts { }
}