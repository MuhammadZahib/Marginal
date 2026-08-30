package com.example.marginal.presentation.scan

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private enum class ScanState { Preview, Processing, NoTextFound }

@Composable
fun ScanScreen(
    onBackClick: () -> Unit,
    onTextRecognized: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasCameraPermission = granted
    }
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    var scanState by remember { mutableStateOf(ScanState.Preview) }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF111310))) {
        if (!hasCameraPermission) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Marginal needs camera access to scan text.", color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Grant camera access")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onBackClick) { Text("Cancel", color = Color.White) }
            }
        } else {
            // Camera preview + binding
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

            LaunchedEffect(Unit) {
                val cameraProvider = context.getCameraProvider()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture,
                )
            }

            // Corner-guide overlay (visual only — doesn't affect what gets captured)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxWidth(0.78f).fillMaxSize(0.55f)) {
                    val strokeWidth = 6f
                    val cornerLen = 40f
                    val color = Color(0xFFD6A64B)
                    drawLine(color, Offset(0f, 0f), Offset(cornerLen, 0f), strokeWidth)
                    drawLine(color, Offset(0f, 0f), Offset(0f, cornerLen), strokeWidth)
                    drawLine(color, Offset(size.width, 0f), Offset(size.width - cornerLen, 0f), strokeWidth)
                    drawLine(color, Offset(size.width, 0f), Offset(size.width, cornerLen), strokeWidth)
                    drawLine(color, Offset(0f, size.height), Offset(cornerLen, size.height), strokeWidth)
                    drawLine(color, Offset(0f, size.height), Offset(0f, size.height - cornerLen), strokeWidth)
                    drawLine(color, Offset(size.width, size.height), Offset(size.width - cornerLen, size.height), strokeWidth)
                    drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - cornerLen), strokeWidth)
                }
            }

            Row(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(12.dp)) {
                TextButton(onClick = onBackClick) { Text("✕ Close", color = Color.White) }
            }

            if (scanState == ScanState.Preview) {
                Text(
                    text = "Align the page within the frame",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.TopCenter).statusBarsPadding().padding(top = 56.dp),
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .border(width = 3.dp, color = Color.White, shape = CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD6A64B))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            if (scanState != ScanState.Preview) return@clickable
                            scanState = ScanState.Processing
                            imageCapture.takePicture(
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageCapturedCallback() {
                                    override fun onCaptureSuccess(image: ImageProxy) {
                                        coroutineScope.launch {
                                            recognizeText(
                                                image = image,
                                                onSuccess = { text ->
                                                    if (text.isBlank()) {
                                                        scanState = ScanState.NoTextFound
                                                    } else {
                                                        onTextRecognized(text)
                                                    }
                                                },
                                                onFailure = { scanState = ScanState.NoTextFound },
                                            )
                                        }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        scanState = ScanState.NoTextFound
                                    }
                                },
                            )
                        },
                )
            }

            if (scanState == ScanState.Processing) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFFD6A64B))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Reading text…", color = Color.White)
                    }
                }
            }

            if (scanState == ScanState.NoTextFound) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Couldn't find any text — try again", color = Color.White)
                        Spacer(modifier = Modifier.height(12.dp))
                        TextButton(onClick = { scanState = ScanState.Preview }) {
                            Text("Retry", color = Color(0xFFD6A64B))
                        }
                    }
                }
            }
        }
    }
}

private suspend fun recognizeText(
    image: ImageProxy,
    onSuccess: (String) -> Unit,
    onFailure: () -> Unit,
) {
    try {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val inputImage = InputImage.fromBitmap(bitmap, image.imageInfo.rotationDegrees)

        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            .process(inputImage)
            .addOnSuccessListener { visionText -> onSuccess(visionText.text) }
            .addOnFailureListener { onFailure() }
            .addOnCompleteListener { image.close() }
    } catch (e: Exception) {
        image.close()
        onFailure()
    }
}

private suspend fun android.content.Context.getCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { continuation ->
        val future = ProcessCameraProvider.getInstance(this)
        future.addListener(
            { continuation.resume(future.get()) },
            ContextCompat.getMainExecutor(this),
        )
    }
