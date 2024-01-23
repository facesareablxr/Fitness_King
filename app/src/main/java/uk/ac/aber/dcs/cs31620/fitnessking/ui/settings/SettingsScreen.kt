package uk.ac.aber.dcs.cs31620.fitnessking.ui.settings


import android.annotation.SuppressLint
import android.widget.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.appbars.SmallTopAppBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SmallTopAppBar(navController = navController, title = stringResource(R.string.Settings))
        },
        content = { _ ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                item {
                   //LightDarkModeToggle()
                }
                item {
                 //   LanguageSelection()
                }
            }
        }
    )
}
/*

@Composable
fun LightDarkModeToggle() {
    val darkModeState = rememberToggleState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Dark Mode")
        Switch(
            checked = darkModeState.value,
            onCheckedChange = { darkModeState.toggle() },
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

fun rememberToggleState(): Any {
    TODO("Not yet implemented")
}

@Composable
fun LanguageSelection() {
    val languages = listOf("English", "German", "Korean", "Arabic")
    val selectedLanguageState = remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Select Language")
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(start = 16.dp)
                .fillMaxWidth() // Ensure the Box takes the full width
        ) {
            Text(text = languages[selectedLanguageState.intValue], modifier = Modifier.padding(16.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEachIndexed { index, language ->
                    DropdownMenuItem(
                        onClick = {
                            selectedLanguageState.intValue = index
                            expanded = false
                            // Implement logic to change language based on selectedLanguage
                            Toast.makeText(LocalContext.current, "Language selected: $language", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = language)
                    }
                }
            }
        }
    }
}*/
