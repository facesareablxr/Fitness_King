package uk.ac.aber.dcs.cs31620.fitnessking.model.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.fitnessking.R
import java.io.File
import java.io.IOException

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ExerciseImage(
    imagePath: String,
    modifier: Modifier,
    updateImagePath: (String) -> Unit = {}
) {
    var photoFile: File? = remember { null }
    val context = LocalContext.current
    val resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                updateImagePath(
                    "file://${photoFile!!.absolutePath}"
                )
            }
        }
    // Should recompose if imagePath changes as a result of taking the picture
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        GlideImage(
            model = Uri.parse(imagePath),
            contentDescription = stringResource(R.string.exerciseImage),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .clickable {
                    takePicture(
                        context = context,
                        resultLauncher = resultLauncher,
                    ) {
                        photoFile = it
                    }
                }
        )
        Text(text = stringResource(id = R.string.enterImage))
    }

}

private fun takePicture(
    context: Context,
    resultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    updateFile: (File) -> Unit
) {
    // Code obtained and adapted from: https://developer.android.com/training/camera/photobasics
    // See configuration instructions added to AndroidManifest.xml
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var photoFile: File? = null

    // Create the File where the photo should go
    try {
        photoFile = ResourceUtil.createImageFile(context)
    } catch (ex: IOException) {
        // Error occurred while creating the File
        Toast.makeText(
            context,
            context.getString(R.string.imageError),
            Toast.LENGTH_SHORT
        ).show()
    }

    // Continue only if the File was successfully created
    photoFile?.let {
        val photoUri = FileProvider.getUriForFile(
            context,
            context.packageName,
            it
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        try {
            resultLauncher.launch(takePictureIntent)
            updateFile(photoFile)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.imageError, Toast.LENGTH_LONG)
                .show()
        }
    }

}
