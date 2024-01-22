package uk.ac.aber.dcs.cs31620.fitnessking.ui.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.content.FileProvider
import uk.ac.aber.dcs.cs31620.fitnessking.R
import uk.ac.aber.dcs.cs31620.fitnessking.ui.util.ResourceUtil
import java.io.File
import java.io.IOException

/**
 * This was again taken from the https://github.com/chriswloftus/feline-adoption-agency-v10/tree/master
 * but it was given its own class for the sake of keeping code short
 */
private fun pictureFromCamera(
    resultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    updateFile: (File) -> Unit,
    context: Context
) {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var photoFile: File? = null
    // Create the File where the photo should go
    try {
        photoFile = ResourceUtil.createImageFile(context)
    } catch (ex: IOException) {
        // Error occurred while creating the File
        Toast.makeText(
            context,
            context.getString(R.string.photoerror),
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
            Toast.makeText(context, R.string.photoerror, Toast.LENGTH_LONG)
                .show()
        }
    }

}