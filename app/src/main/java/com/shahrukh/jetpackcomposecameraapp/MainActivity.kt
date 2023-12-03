package com.shahrukh.jetpackcomposecameraapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.*
import com.shahrukh.jetpackcomposecameraapp.ui.theme.JetpackComposeCameraAppTheme

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext


@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {



            JetpackComposeCameraAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    cameraScreen()
                }
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun cameraScreen(

) {

    var imageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }

      val context = LocalContext.current


    val file = context.createImageFile()

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.shahrukh.jetpackcomposecameraapp.fileProvider", file
    )

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )


    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    imageUri = Uri.parse(data.data.toString())

                    if (imageUri.toString().isNotEmpty()) {
                        Log.d("myImageUri", "$imageUri ")
                    }
                }
            }
        }




    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) imageUri = uri
            if (imageUri.toString().isNotEmpty()) {
                Log.d("myImageUri", "$imageUri ")
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
            permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
        ) {
            Toast.makeText(context, "Gallery Permissions Granted", Toast.LENGTH_SHORT).show()
            // Launch gallery intent here
            galleryLauncher.launch(galleryIntent)
        } else {
            Toast.makeText(context, "Gallery Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }



    val mediaPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(
           // Manifest.permission.READ_MEDIA_IMAGES android.Manifest.permission.READ_MEDIA_IMAGES
        ) else listOf(
           // Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
           // Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )



    val hasCameraPermission = cameraPermissionState.status.isGranted
    val hasMediaPermission = mediaPermissionState.allPermissionsGranted

    // Launch camera or gallery based on permission state
    /**DisposableEffect(Unit) {
        if (cameraPermissionState.status.isGranted) {
            cameraLauncher.launch(uri)
        } else if (mediaPermissionState.allPermissionsGranted) {
            galleryLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
        onDispose { }
    }*/

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {






        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(16.dp)
                .clickable {
                    if (hasCameraPermission) {
                        cameraLauncher.launch(uri)
                    } else {
                       // cameraPermissionState.launchPermissionRequest()
                        // Request a permission
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                    // Request camera permission





                },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //ImageFromRes(imageRes = R.drawable.ic_gallery)
            Text(text = "Take Photo")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(16.dp)
                .clickable {
                    if (hasMediaPermission) {
                        galleryLauncher.launch(galleryIntent)
                    } else {
                        //mediaPermissionState.launchMultiplePermissionRequest()
                        //permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE )
                        galleryPermissionLauncher.launch(
                            arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )

                    }
                },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            //ImageFromRes(imageRes = R.drawable.ic_camera)
            //AppText(textValue = "Take a photo" )
            Text(text = "Select from gallery")
        }


    }




}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir /* directory */
    )
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeCameraAppTheme {
        Greeting("Android")
    }
}