package uk.ac.aber.dcs.cs31620.fitnessking.ui.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.fitnessking.ui.home.TodaysWorkout

/**
 * Represents the home screen. For this version we only have a
 * top app bar and empty content.
 * @author Chris Loftus
 */
@Composable
fun SavedWorkoutsScreen(navController: NavHostController) {
        TopLevelScaffold(
            navController = navController
        )
        { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
            }}}

@Composable
private fun SavedWorkoutsScreenContent(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.date),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        TodaysWorkout()
    }
}


@Composable
fun SearchFunction(navController: NavHostController){
    var selected by remember { mutableStateOf(false) }
    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text("Search")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        },
        selected = selected

    )

}

@Composable
fun DropSetSelect(navController: NavHostController){
    var selected by remember { mutableStateOf(false) }
    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text("Drop Set")
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Preview
@Composable
private fun SavedWorkoutsScreen(){
    val navController = rememberNavController()
    FitnessKingTheme(dynamicColor = false) {
        SavedWorkoutsScreen(navController)
    }
}
