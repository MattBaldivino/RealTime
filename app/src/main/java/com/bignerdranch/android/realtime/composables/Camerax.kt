package com.bignerdranch.android.realtime.composables

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.bignerdranch.android.realtime.Posts
import database.AppDatabase
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable

fun CameraPreviewScreen(navController: NavHostController) {

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.surfaceProvider = previewView.surfaceProvider
    }
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        // Capture image button
        Button(
            onClick = { captureImage(imageCapture, context) },
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
            Text(text = "Capture Image")
        }

        // Second button for returning to home page
        Button(
            onClick = {
                navController.navigate("home") // Navigate to the Camera Screen
            },
            modifier = Modifier.padding(bottom = 40.dp) // Add padding to position below the first button
        ) {
            Text(text = "Home")
        }
    }
}

private fun captureImage(imageCapture: ImageCapture, context: Context) {
    val name = "CameraxImage.jpeg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println("Success")
                val uri = outputFileResults.savedUri
                println("Image saved at: $uri")

                val imageByteArray = context.contentResolver.openInputStream(uri!!)?.use { it.readBytes() }

                // Save image data to database (Assuming you're saving it in Posts table)
                val appDatabase = AppDatabase.getDatabase(context)
                val postsDao = appDatabase.postsDao()

                // Create a new Post object to insert into the database
                val post = Posts(
                    photoFileName = name,
                    date = Date().date,
                    owner = "User",
                    image = imageByteArray // Save the image byte array
                )

                // Insert the post into the database
                val postId = postsDao.insertPost(post)
                if (postId > 0) {
                    // Successfully inserted the post, the ID is greater than 0
                    println("Post inserted successfully with ID: $postId")
                } else {
                    // Insertion failed
                    println("Failed to insert post.")
                }
            }

            override fun onError(exception: ImageCaptureException) {
                println("Failed $exception")
            }

        })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }
