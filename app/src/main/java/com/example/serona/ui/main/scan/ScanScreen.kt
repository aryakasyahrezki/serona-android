package com.example.serona.ui.main.scan

import android.content.Context
import android.graphics.Bitmap
import android.net.wifi.ScanResult
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.serona.R
import com.example.serona.data.model.FaceDetectionResponse
import com.example.serona.theme.Black10
import com.example.serona.theme.Grey40
import com.example.serona.theme.MutedLight
import com.example.serona.theme.Primary
import com.example.serona.theme.White
import com.example.serona.theme.figtreeFontFamily
import com.example.serona.ui.navigation.Routes
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors
import androidx.compose.runtime.remember
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import android.util.Range

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel(),
    navController: NavController
) {

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val progress by viewModel.progress
    val isScanning by viewModel.isScanning
    val showPopup by viewModel.showInstructionPopup
    val showResultBtn by viewModel.showRecommendationButton
    val scanResult by viewModel.scanResult

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp
    val minDimension = if (maxWidth < maxHeight) maxWidth else maxHeight

    val fontSize = (maxWidth.value * 0.05f).sp

    var lastProcessedTime by remember { mutableLongStateOf(0L) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }


//    LaunchedEffect(progress) {
//        if (progress >= 1.0f && isScanning) {
//            takePhoto(context, imageCapture) { file ->
//                viewModel.onFirstScanComplete(file)
//            }
//        }
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        // LAPISAN 1: Kamera
        AndroidView(
            factory = { ctx ->
                // 1. Inisialisasi PreviewView hanya SEKALI
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                // 2. Hal ringan seperti Mirroring ditaruh di update (tidak bikin restart kamera)
                previewView.scaleX = if (lensFacing == CameraSelector.LENS_FACING_FRONT) -1f else 1f

                // 3. Setup Kamera Utama
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    // KUNCI: Cek apakah kamera perlu di-bind ulang (hanya saat pindah lensa)
                    // Ini mencegah kamera 'stutter' atau restart setiap kali progress bar jalan
                    cameraProvider.unbindAll()

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()

                    val resolutionSelector = ResolutionSelector.Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy(AspectRatio.RATIO_4_3, AspectRatioStrategy.FALLBACK_RULE_AUTO))
                        .build()

                    val preview = Preview.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .build()
                        .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .also { builder ->
                            // PAKSA hardware kamera ke mode 30 FPS (atau range 30-30)
                            val ext: Camera2Interop.Extender<*> = Camera2Interop.Extender(builder)
                            ext.setCaptureRequestOption(
                                android.hardware.camera2.CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
                                android.util.Range(30, 30)
                            )
                        }
                        .build()

                    imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
                        val currentTime = System.currentTimeMillis()

                        // LIMITER & LOCK:
                        // Hanya kirim data jika: Tidak sedang upload && Jedanya > 400ms && Belum kelar
                        if (viewModel.isScanning.value &&
                            !viewModel.isCurrentlyUploading.value &&
                            viewModel.progress.value < 1.0f &&
                            currentTime - lastProcessedTime > 800L
                        ) {
                            lastProcessedTime = currentTime
                            processFaceDetection(imageProxy, viewModel, context)
                        } else {
                            imageProxy.close() // Buang frame sampah secepat mungkin
                        }
                    }

                    try {
                        // Jalankan binding
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )

                        // MATIKAN fokus otomatis yang bikin kamera 'muter-muter'
                        camera.cameraControl.cancelFocusAndMetering()
                        camera.cameraControl.enableTorch(false)


                    } catch (e: Exception) {
                        android.util.Log.e("CameraX", "Binding failed", e)
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )

        // LAPISAN 2: UI Content
        ScanUIContent(
            navController = navController,
            viewModel = viewModel,
            progress = progress,
            scanResult = scanResult,
            showResultBtn = showResultBtn,
            showPopup = showPopup,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            minDimension = minDimension, // Kirim patokan responsif
            onSwitchCamera = {
                // Logika tukar kamera
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                    CameraSelector.LENS_FACING_BACK
                } else {
                    CameraSelector.LENS_FACING_FRONT
                }
            }
        )

        // LAPISAN 3: Overlay Dialog
        if (showPopup) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Black10.copy(alpha = 0.78f))
                .pointerInput(Unit) {}
            )
            InstructionDialog(onDismiss = { viewModel.dismissPopup() })
        }
    }
}

@Composable
fun ScanUIContent(
    navController: NavController,
    viewModel: ScanViewModel,
    progress: Float,
    scanResult: FaceDetectionResponse?,
    showResultBtn: Boolean,
    showPopup: Boolean,
    maxWidth: Dp,
    maxHeight: Dp,
    minDimension: Dp, // Tambahkan ini untuk patokan responsif kotak
    onSwitchCamera: () -> Unit // Tambahkan ini untuk aksi tombol
) {
    // Patokan font tetap menggunakan minDimension agar tidak berubah drastis saat rotasi
    val baseFontSize = (minDimension.value * 0.05f).sp
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec // Animasi default yang smooth
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. LAYER HEADER: Back Button, Switch Button & Judul
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = maxWidth * 0.06f)
//                .padding(top = maxHeight * 0.05f, start = maxWidth * 0.06f, end = maxWidth * 0.06f)
        ) {
            // Row untuk menaruh Back Button dan Switch Camera berseberangan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (!showPopup) {
                            viewModel.stopScanning()
                            navController.navigate(Routes.SCAN_MENU) {
                                popUpTo(Routes.SCAN_MENU) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(minDimension * 0.12f) // Gunakan minDimension agar tetap lingkaran sempurna
                        .background(White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = White,
                        modifier = Modifier.size(minDimension * 0.06f)
                    )
                }

                // --- TOMBOL SWITCH CAMERA (TAMBAHAN BARU) ---
                IconButton(
                    onClick = onSwitchCamera,
                    modifier = Modifier
                        .size(minDimension * 0.12f)
                        .background(White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_switch_camera),
                        contentDescription = "Switch Camera",
                        tint = White,
                        modifier = Modifier.size(minDimension * 0.06f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(maxHeight * 0.005f))

            Text(
                text = "Face Recognition",
                fontSize = baseFontSize * 1.4f, // 1.4x dari base font
                fontWeight = FontWeight.Bold,
                color = White,
                fontFamily = figtreeFontFamily
            )
            Text(
                text = "Please look at the camera and stay still",
                fontSize = baseFontSize * 0.7f,
                color = Color.White.copy(alpha = 0.8f),
                fontFamily = figtreeFontFamily
            )

            if (scanResult != null && !showPopup) {
                Spacer(modifier = Modifier.height(maxHeight * 0.02f))

                Card(
                    modifier = Modifier.wrapContentWidth().align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.9f)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Gunakan baseFontSize agar ukuran teks di dalam item ikut responsif
                        ResultSummaryItem(label = "Face Shape", value = scanResult.shape ?: "-", minDimension)

                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.LightGray))

                        ResultSummaryItem(label = "Skintone", value = scanResult.skintone ?: "-", minDimension)
                    }
                }
            }
        }

        // 2. LAYER SCANNING
        if (!showPopup) {
            if (maxWidth < maxHeight) {
                FaceGuideFrame(
                    modifier = Modifier.align(Alignment.Center),
                    size = maxWidth * 0.75f
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = if (maxWidth < maxHeight) maxHeight * 0.06f else 10.dp,
                        start = maxWidth * 0.1f,
                        end = maxWidth * 0.1f
                    )
                    .fillMaxWidth(if (maxWidth > maxHeight) 0.6f else 1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (showResultBtn) "Live Scanning..." else "Hold still...",
                    color = Color.White,
                    fontSize = baseFontSize * 0.8f,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(minDimension * 0.025f)
                        .clip(RoundedCornerShape(minDimension * 0.01f)),
                    color = Color(0xFFADFF2F),
                    trackColor = Color.White.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(maxHeight * 0.03f))

                Button(
                    onClick = {
                        if (showResultBtn) {
                            val shape = scanResult?.shape ?: "Unknown"
                            val color = scanResult?.skintone ?: "Unknown"
                            navController.navigate("result/$shape/$color") {
                                popUpTo(Routes.SCAN) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(maxHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showResultBtn) Primary else White.copy(alpha = 0.2f),
                        contentColor = if (showResultBtn) White else Color.Gray
                    ),
                    shape = RoundedCornerShape(14.dp),
                    enabled = true
                ) {
                    Text("Analysis Ready - See Result", fontSize = baseFontSize * 0.8f, fontWeight = FontWeight.Bold, color = White)
                }
            }
        }
    }
}

@Composable
fun ResultSummaryItem(
    label: String,
    value: String,
    minDimension: Dp // Kita ganti maxWidth jadi minDimension
) {
    // Menghitung font size berdasarkan 5% dari sisi terpendek layar (baseFontSize)
    // Lalu kita skala: label (0.6x) dan value (0.8x) dari base tersebut
    val baseFontSize = (minDimension.value * 0.05f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = label,
            // Hasilnya akan sama dengan ~0.03f maxWidth pada Portrait,
            // tapi stabil saat Landscape.
            fontSize = (baseFontSize * 0.6f).sp,
            color = Black10,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            // Hasilnya akan sama dengan ~0.04f maxWidth pada Portrait
            fontSize = (baseFontSize * 0.8f).sp,
            fontWeight = FontWeight.Bold,
            color = Primary, // Tetap Warna Pink khas Serona
            fontFamily = figtreeFontFamily
        )
    }
}

@Composable
fun FaceGuideFrame(modifier: Modifier, size: Dp) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 8.dp.toPx() // Ketebalan garis
        val cornerSize = size.toPx() * 0.2f // Panjang garis di setiap sudut
        val radius = 24.dp.toPx() // Tingkat kelengkungan (rounded) sudut
        val canvasSize = size.toPx()

        val guideColor = Color.White.copy(alpha = 0.9f)

        // 1. KIRI ATAS
        drawPath(
            path = Path().apply {
                moveTo(0f, cornerSize)
                lineTo(0f, radius)
                quadraticBezierTo(0f, 0f, radius, 0f)
                lineTo(cornerSize, 0f)
            },
            color = guideColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 2. KANAN ATAS
        drawPath(
            path = Path().apply {
                moveTo(canvasSize - cornerSize, 0f)
                lineTo(canvasSize - radius, 0f)
                quadraticBezierTo(canvasSize, 0f, canvasSize, radius)
                lineTo(canvasSize, cornerSize)
            },
            color = guideColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 3. KANAN BAWAH
        drawPath(
            path = Path().apply {
                moveTo(canvasSize, canvasSize - cornerSize)
                lineTo(canvasSize, canvasSize - radius)
                quadraticBezierTo(canvasSize, canvasSize, canvasSize - radius, canvasSize)
                lineTo(canvasSize - cornerSize, canvasSize)
            },
            color = guideColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 4. KIRI BAWAH
        drawPath(
            path = Path().apply {
                moveTo(cornerSize, canvasSize)
                lineTo(radius, canvasSize)
                quadraticBezierTo(0f, canvasSize, 0f, canvasSize - radius)
                lineTo(0f, canvasSize - cornerSize)
            },
            color = guideColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun InstructionDialog(onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    // Trik Responsif: Gunakan sisi terpendek sebagai patokan
    val minDimension = if (maxWidth < maxHeight) maxWidth else maxHeight
    val baseFontSize = (minDimension.value * 0.05f).sp

    Dialog(onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier
                // Saat landscape, jangan terlalu lebar (70% saja), saat portrait 90%
                .width(if (maxWidth > maxHeight) maxWidth * 0.7f else maxWidth * 0.9f)
                .wrapContentHeight()
                .padding(vertical = 16.dp), // Beri jarak aman dari tepi layar
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            // Tambahkan scroll di sini agar aman di mode Landscape
            Column(
                modifier = Modifier
                    .padding(minDimension * 0.06f, minDimension * 0.05f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Face Scanning Guide",
                    fontSize = baseFontSize,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(minDimension * 0.02f))
                Text(
                    text = "Follow these steps to get the most accurate results",
                    fontSize = baseFontSize * 0.65f,
                    textAlign = TextAlign.Center,
                    color = Grey40,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(minDimension * 0.03f))

                // List Panduan (Kirim minDimension ke Item)
                InstructionItem("Don't make excessive expressions", "Keep your face relaxed", R.drawable.scan_guide1)
                InstructionItem("Don't cover your face", "Avoid glasses or masks", R.drawable.scan_guide2)
                InstructionItem("Avoid poor lighting", "Avoid shadows or dim light", R.drawable.scan_guide3)
                InstructionItem("Don't tilt your head", "Keep your head straight", R.drawable.scan_guide4)

                Spacer(modifier = Modifier.height(minDimension * 0.05f))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(minDimension * 0.4f)
                        .height(minDimension * 0.12f), // Ukuran tombol proporsional
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF04B63)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Start", fontSize = baseFontSize * 0.7f, fontWeight = FontWeight.Bold, color = White)
                }
            }
        }
    }
}

@Composable
fun InstructionItem(title: String, description: String, imageRes: Int) {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    // Gunakan sisi terpendek sebagai patokan ukuran
    val minDimension = if (maxWidth < maxHeight) maxWidth else maxHeight

    val fontSize = (minDimension.value * 0.05f).sp
    val imgSize = minDimension * 0.15f // Gambar tetap proporsional terhadap sisi terpendek

    Row(
        modifier = Modifier
            .fillMaxWidth() // Gunakan fillMaxWidth agar mengisi lebar dialog
            .padding(vertical = minDimension * 0.025f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Box pembungkus Image & Icon
        Box(modifier = Modifier.size(imgSize)) {
            // Box 2: Image dengan Rounded Corner
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(imgSize).align(Alignment.BottomStart)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Box 3: Icon X di Top End
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Grey40),
                modifier = Modifier.size(imgSize * 0.3f).align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(minDimension * 0.04f))

        // Bagian Teks di sebelah kanan gambar
        Column {
            Text(
                text = title,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = fontSize * 0.65f,
                letterSpacing = 0.4.sp,
                lineHeight = 10.sp // Tetap sesuai aslimu
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = description,
                fontSize = fontSize * 0.6f,
                color = Grey40,
                lineHeight = 16.sp // Tetap sesuai aslimu
            )
        }
    }
}

private val faceDetector = FaceDetection.getClient(
    FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()
)

@OptIn(ExperimentalGetImage::class)
fun processFaceDetection(imageProxy: ImageProxy, viewModel: ScanViewModel, context: Context) {
    val mediaImage = imageProxy.image ?: return imageProxy.close()
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    // Gunakan faceDetector yang sudah ada di luar, jangan buat Builder() lagi di sini
    faceDetector.process(image)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                // 1. Ambil Bitmap secepat mungkin
                val bitmap = imageProxy.toBitmap()

                // 2. Jalankan proses simpan & kirim di background total
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val file = File(context.cacheDir, "face_scan.jpg")
                        file.outputStream().use { out ->
                            // Pakai kualitas 30-40 saja agar file kecil (AI tidak butuh kualitas 4K)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 35, out)
                        }
                        // Kirim ke ViewModel
                        viewModel.onFaceDetected(file)
                    } catch (e: Exception) {
                        Log.e("SCAN_ERROR", "Gagal simpan frame: ${e.message}")
                    }
                }
            } else {
                viewModel.onFaceLost()
            }
        }
        .addOnCompleteListener {
            // Selalu tutup imageProxy agar antrean frame berikutnya lancar
            imageProxy.close()
        }
}

//fun takePhoto(context: Context, imageCapture: ImageCapture?, onImageCaptured: (File) -> Unit) {
//    val ic = imageCapture ?: return
//    val photoFile = File(context.cacheDir, "scan_${System.currentTimeMillis()}.jpg")
//    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//    ic.takePicture(outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
//        override fun onImageSaved(output: ImageCapture.OutputFileResults) { onImageCaptured(photoFile) }
//        override fun onError(exc: ImageCaptureException) { exc.printStackTrace() }
//    })
//}