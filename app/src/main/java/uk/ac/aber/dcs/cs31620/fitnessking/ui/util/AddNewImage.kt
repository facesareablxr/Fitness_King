package uk.ac.aber.dcs.cs31620.fitnessking.ui.util

import android.app.Activity
import android.app.Activity.RESULT_OK
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import uk.ac.aber.dcs.cs31620.fitnessking.R
import java.io.File
import java.io.IOException

/**
 * Taken from feline-adoption-agency-v10, but moving it into a separate file for the sake of keeping
 * the classes it is used in shorter.
 *
 */

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddNewImage(
    imagePath: String,
    modifier: Modifier,
    updateImagePath: (String) -> Unit = {}
) {
    var photoFile: File? = remember { null }
    val ctx = LocalContext.current


    val resultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
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
                        ctx = ctx,
                        resultLauncher = resultLauncher,
                    ) {
                        photoFile = it
                    }
                },
        )
        Text(text = stringResource(id = R.string.enterImage))
    }

}

private fun takePicture(
    ctx: Context,
    resultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    updateFile: (File) -> Unit
) {
    // Code obtained and adapted from: https://developer.android.com/training/camera/photobasics
    // See configuration instructions added to AndroidManifest.xml
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var photoFile: File? = null

    // Create the File where the photo should go
    try {
        photoFile = ResourceUtil.createImageFile(ctx)
    } catch (ex: IOException) {
        // Error occurred while creating the File
        Toast.makeText(
            ctx,
            ctx.getString(R.string.imageError),
            Toast.LENGTH_SHORT
        ).show()
    }

    // Continue only if the File was successfully created
    photoFile?.let {
        val photoUri = FileProvider.getUriForFile(
            ctx,
            ctx.packageName,
            it
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        // Request will fail if a camera app not available.
        // This used to use takePictureIntent.resolveActivity(requireActivity().packageManager)
        // However this requires a <query> element to be added to the manifest for
        // Android 30+. The following is simpler.
        try {
            resultLauncher.launch(takePictureIntent)
            updateFile(photoFile)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(ctx, R.string.photoerror, Toast.LENGTH_LONG)
                .show()
        }
    }

}