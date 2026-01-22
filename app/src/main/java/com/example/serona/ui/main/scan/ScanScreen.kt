package com.example.serona.ui.main.scan

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.util.concurrent.Executors

@Composable
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val progress by viewModel.progress
    val isScanning by viewModel.isScanning
    val showPopup by viewModel.showInstructionPopup
    val showResultBtn by viewModel.showRecommendationButton
    val scanResult by viewModel.scanResult

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    LaunchedEffect(progress) {
        if (progress >= 1.0f && isScanning) {
            takePhoto(context, imageCapture) { file ->
                viewModel.onFirstScanComplete(file)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    scaleX = -1f

                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    cameraProvider.unbindAll()

                    // DINAMIS: Mencari kamera apa pun yang tersedia di emulator
                    val cameraInfos = cameraProvider.availableCameraInfos
                    if (cameraInfos.isEmpty()) {
                        android.util.Log.e("CAMERA_ERROR", "Tidak ada kamera ditemukan")
                        return@addListener
                    }

                    // 1. Definisikan strategi rasio 16:9 agar FULL SCREEN
                    val resolutionSelector = ResolutionSelector.Builder()
                        .setAspectRatioStrategy(
                            AspectRatioStrategy(
                                AspectRatio.RATIO_16_9,
                                AspectRatioStrategy.FALLBACK_RULE_AUTO
                            )
                        )
                        .build()

                    // 2. Pilih kamera pertama yang tersedia
                    val cameraSelector = cameraInfos.first().cameraSelector

                    // 3. Setup Preview dengan ResolutionSelector
                    val preview = Preview.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .build()
                        .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                    // 4. Setup ImageCapture (untuk kirim ke API) dengan rasio yang sama
                    imageCapture = ImageCapture.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    // 5. Setup ImageAnalysis (untuk deteksi wajah) dengan rasio yang sama
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                        processFaceDetection(imageProxy, viewModel, context)
                    }

                    try {
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        if (showPopup) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)))
            InstructionDialog(onDismiss = { viewModel.dismissPopup() })
        }

        if (!showPopup) {
            // Gunakan satu Column untuk semua elemen UI agar tersusun vertikal
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp, start = 32.dp, end = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Card Hasil (Muncul jika API sudah balik)
                scanResult?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Face Shape: ${it.shape}", color = Color.White)
                            Text(text = "Skintone: ${it.skintone ?: "Scanning..."}", color = Color.White)
                        }
                    }
                }

                // 2. Progress Bar (Selalu muncul agar Rescan terlihat)
                Text(
                    text = if (showResultBtn) "Rescanning..." else "Hold still...",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = Color(0xFF4CAF50),
                    trackColor = Color.White.copy(alpha = 0.3f)
                )

                // 3. Tombol Rekomendasi (Hanya muncul jika sudah ada hasil pertama)
                if (showResultBtn) {
                    Spacer(modifier = Modifier.height(24.dp)) // Jarak agar tidak menempel ke Bar
                    Button(
                        onClick = {
                            val shape = scanResult?.shape ?: "Unknown"
                            val color = scanResult?.skintone ?: "Unknown"
                            // Navigasi dilakukan di sini nanti
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("See Recommendation", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showResultBtn) {
            Button(
                onClick = {
                    val shape = scanResult?.shape ?: "Unknown"
                    val color = scanResult?.skintone ?: "Unknown"
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("See Recommendation", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InstructionDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Panduan Pemindaian", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("1. Cahaya cukup\n2. Wajah lurus ke depan\n3. Tunggu hingga bar selesai", lineHeight = 24.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))) {
                    Text("Mulai")
                }
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
fun processFaceDetection(imageProxy: ImageProxy, viewModel: ScanViewModel, context: Context) {
    val mediaImage = imageProxy.image ?: return imageProxy.close()
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
    val detector = FaceDetection.getClient(FaceDetectorOptions.Builder().setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST).build())
    detector.process(image)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                viewModel.onFaceDetected()
            } else {
                viewModel.onFaceLost()
            }
        }
        .addOnCompleteListener { imageProxy.close() }
}

fun takePhoto(context: Context, imageCapture: ImageCapture?, onImageCaptured: (File) -> Unit) {
    val ic = imageCapture ?: return
    val photoFile = File(context.cacheDir, "scan_${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    ic.takePicture(outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(output: ImageCapture.OutputFileResults) { onImageCaptured(photoFile) }
        override fun onError(exc: ImageCaptureException) { exc.printStackTrace() }
    })
}