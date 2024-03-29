package uk.ac.aber.dcs.cs31620.fitnessking.ui.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private const val FK_TAG = "FK"

/**
 * Utility object for handling photo-related operations. Adapted from FAA V10, Chris Loftus
 */
object ResourceUtil {
    /**
     * Gets the file URI for the photo based on the provided fileName.
     *
     * @param context is the context of the application.
     * @param fileName is the name of the file.
     * @return the File representing the photo file with the specified fileName.
     */
    fun getPhotoFileUri(context: Context, fileName: String): File {
        // Get safe storage directory for photos
        val mediaStorageDir: File =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FK_TAG)

        // Create the storage directory
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(FK_TAG, "failed to create directory")
        }

        // Return the file target
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    /**
     * Creates an image file with a unique name in the application's external files directory.
     *
     * @param context is the context of the application.
     * @return the created File representing the image file.
     */
    @SuppressLint("SimpleDateFormat")
    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm-ss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }
}