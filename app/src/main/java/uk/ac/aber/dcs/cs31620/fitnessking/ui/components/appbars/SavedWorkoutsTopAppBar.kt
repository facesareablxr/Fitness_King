import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.theme.FitnessKingTheme
import uk.ac.aber.dcs.cs31620.fitnessking.R

@Composable
fun SavedWorkoutsTopAppBar(
            onClick: () -> Unit = {}
    ){
        CenterAlignedTopAppBar(
            title = { Text(stringResource(id = R.string.savedWorkouts)) },
            navigationIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nav_drawer_menu)
                    )
                }
            }
        )
    }

@Preview
@Composable
private fun SmallTopAppBarPreview() {
    FitnessKingTheme(dynamicColor = false) {
        SmallTopAppBar()
    }
}
