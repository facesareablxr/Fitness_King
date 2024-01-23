package uk.ac.aber.dcs.cs31620.fitnessking.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.components.TopLevelScaffold

/**
 * This is a very basic profile page, it was one of the last things I made, I would have hoped to
 * have added the option to change the details but due to time restrictions, I could not.
 * It has a standard image for the account photo, and a pre-made username, which is just my own name.
 * It also has an example email, which would have been used to create an account if I had the time.
 */
@Composable
fun ProfileScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        pageContent = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    // Add profile image in a circle
                    AccountImage(
                        imageResource = R.drawable.baseline_account_circle_24,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        // This is the Username text field
                        OutlinedTextField(
                            value = stringResource(R.string.username),
                            onValueChange = { },
                            label = { Text(stringResource(id = R.string.userBox)) },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        //This is the email text field
                        OutlinedTextField(
                            value = stringResource(R.string.email),
                            onValueChange = { },
                            label = { Text(stringResource(id = R.string.emailBox)) },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    )
}

/**
 * This is the painter for the account image, it will return the profile image to the screen
 */
@Composable
fun AccountImage(
    imageResource: Int,
    modifier: Modifier = Modifier
) {
    // Load your image using painter
    val painter = painterResource(id = imageResource)
    Image(
        painter = painter,
        contentDescription = "profile image",
        modifier = modifier
    )
}
