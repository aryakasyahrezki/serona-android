package com.example.serona.ui.main.scan

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.*
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.serona.theme.*
import com.example.serona.ui.navigation.Routes
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors


/**
 * Main screen for the real-time face scanning feature.
 * Coordinates CameraX for previewing, ML Kit for on-device face detection,
 * and responsive UI components for user guidance.
 */
@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel(),
    navController: NavController
) {
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }

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

    var lastProcessedTime by remember { mutableLongStateOf(0L) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    /**
     * CAMERA CONFIGURATION & BINDING
     * Initializes CameraX use cases including Preview and ImageAnalysis.
     * Analysis is throttled to prevent CPU overhead while maintaining scanning stability.
     */
    LaunchedEffect(lensFacing, previewView) {
        val pv = previewView ?: return@LaunchedEffect

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
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
                .also { it.setSurfaceProvider(pv.surfaceProvider) }

            val imageAnalysis = ImageAnalysis.Builder()
                .setResolutionSelector(resolutionSelector)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .also { builder ->
                    val ext: Camera2Interop.Extender<*> = Camera2Interop.Extender(builder)
                    ext.setCaptureRequestOption(
                        android.hardware.camera2.CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE,
                        android.util.Range(30, 30)
                    )
                }
                .build()

            imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
                val currentTime = System.currentTimeMillis()

                // Logic to throttle frame processing to ~800ms intervals during scanning
                if (isScanning &&
                    !viewModel.isCurrentlyUploading.value &&
                    viewModel.progress.value < 1.0f &&
                    currentTime - lastProcessedTime > 800L
                ) {
                    lastProcessedTime = currentTime
                    processFaceDetection(imageProxy, viewModel, context)
                } else {
                    imageProxy.close()
                }
            }

            try {
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("CameraX", "Binding failed", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }.also { previewView = it }
            },
            modifier = Modifier.fillMaxSize(),
            update = { pv ->
                // Mirrors the front camera for a more natural user experience
                pv.scaleX = if (lensFacing == CameraSelector.LENS_FACING_FRONT) -1f else 1f
            }
        )

        ScanUIContent(
            navController = navController,
            viewModel = viewModel,
            progress = progress,
            scanResult = scanResult,
            showResultBtn = showResultBtn,
            showPopup = showPopup,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            minDimension = minDimension,
            onSwitchCamera = {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                    CameraSelector.LENS_FACING_BACK
                } else {
                    CameraSelector.LENS_FACING_FRONT
                }
            }
        )

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

/**
 * Main UI overlay for the scan screen.
 * Displays control buttons, progress indicators, and analysis status.
 */
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
    minDimension: Dp,
    onSwitchCamera: () -> Unit
) {
    val isFaceInFrame = viewModel.isFaceInFrame.value
    val baseFontSize = (minDimension.value * 0.05f).sp
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "ProgressAnimation"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = maxWidth * 0.06f, vertical = maxWidth * 0.03f)
        ) {
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
                        .size(minDimension * 0.08f)
                        .background(White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = White,
                        modifier = Modifier.size(minDimension * 0.06f)
                    )
                }

                IconButton(
                    onClick = onSwitchCamera,
                    modifier = Modifier
                        .size(minDimension * 0.08f)
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

            Spacer(modifier = Modifier.height(maxHeight * 0.02f))

            Text(
                text = "Face Recognition",
                fontSize = baseFontSize * 1.4f,
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

            // Result Preview Card (Visible after successful scan)
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
                        ResultSummaryItem(label = "Face Shape", value = scanResult.shape ?: "-", minDimension)
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.LightGray))
                        ResultSummaryItem(label = "Skintone", value = scanResult.skintone ?: "-", minDimension)
                    }
                }
            }
        }

        if (!showPopup) {
            // Face Frame Guide
            if (maxWidth < maxHeight) {
                FaceGuideFrame(
                    modifier = Modifier.align(Alignment.Center),
                    size = maxWidth * 0.75f
                )
            }

            // Bottom Progress & Action UI
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
                    text = when {
                        // Jika progres belum penuh tapi deteksi berhenti (wajah tertutup/hilang)
                        !isFaceInFrame -> "Face Lost! Move back into frame"
                        showResultBtn -> "Live Scanning..."
                        else -> "Hold still, scanning your beauty..."
                    },
//                    text = if (showResultBtn) "Live Scanning..." else "Hold still...",
                    color = Color.White,
                    fontSize = baseFontSize * 0.8f,
                    modifier = Modifier.padding(bottom = 8.dp)
                )



                LinearProgressIndicator(
//                    progress = { if (isFaceInFrame) 1f else 0f },
                    progress = {
                        when {
                            // 1. Jika sudah masuk fase Live Scanning (Hasil sudah pernah muncul)
                            // Cukup tampilkan 1f jika wajah ada, 0f jika tidak ada.
                            showResultBtn -> if (isFaceInFrame) 1f else 0f

                            // 2. Jika fase Initial Scan (Belum pernah muncul hasil)
                            // Gunakan animasi progress yang berjalan pelan dari 0 ke 1.
                            else -> animatedProgress
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(minDimension * 0.025f)
                        .clip(RoundedCornerShape(minDimension * 0.01f))
                        .alpha(if (isFaceInFrame) shimmerAlpha else 1f),
                    color = SuccessScan,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(maxHeight * 0.03f))

                Button(
                    onClick = {
                        if (showResultBtn) {
                            val shape = scanResult?.shape ?: "Unknown"
                            val color = scanResult?.skintone ?: "Unknown"
                            navController.navigate("result/$shape/$color") {
                                popUpTo(Routes.SCAN) { inclusive = true } }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.9f).height(maxHeight * 0.07f),
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

/**
 * Summary item for the scanning results (Face Shape/Skintone).
 */
@Composable
fun ResultSummaryItem(label: String, value: String, minDimension: Dp) {
    val baseFontSize = (minDimension.value * 0.05f)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(text = label, fontSize = (baseFontSize * 0.6f).sp, color = Black10, fontFamily = figtreeFontFamily, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = (baseFontSize * 0.8f).sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = figtreeFontFamily)
    }
}

/**
 * Decorative UI frame that helps user position their face for analysis.
 */
@Composable
fun FaceGuideFrame(modifier: Modifier, size: Dp) {
    Canvas(modifier = modifier.size(size)) {
        val strokeWidth = 8.dp.toPx()
        val cornerSize = size.toPx() * 0.2f
        val radius = 24.dp.toPx()
        val canvasSize = size.toPx()
        val guideColor = Color.White.copy(alpha = 0.9f)

        // Rendering corners using quadratic bezier curves for rounded guide
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

/**
 * Modal dialog displaying scanning instructions.
 */
@Composable
fun InstructionDialog(onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current
    val minDimension = if (configuration.screenWidthDp < configuration.screenHeightDp) configuration.screenWidthDp.dp else configuration.screenHeightDp.dp
    val baseFontSize = (minDimension.value * 0.05f).sp

    Dialog(onDismissRequest = { }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier.width(if (configuration.screenWidthDp > configuration.screenHeightDp) configuration.screenWidthDp.dp * 0.7f else configuration.screenWidthDp.dp * 0.9f).wrapContentHeight().padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(minDimension * 0.06f, minDimension * 0.05f).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Face Scanning Guide", fontSize = baseFontSize, fontFamily = figtreeFontFamily, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(minDimension * 0.01f))
                Text(text = "Follow these steps below while scanning to get the most accurate results", fontSize = baseFontSize * 0.65f, textAlign = TextAlign.Center, color = Grey40.copy(alpha = 0.8f), lineHeight = baseFontSize * 0.8f)
                Spacer(modifier = Modifier.height(minDimension * 0.03f))
                InstructionItem( "Don't make excessive expressions","Keep your face relaxed and natural", R.drawable.scan_guide1)
                InstructionItem( "Don't cover your face","Avoid glasses, masks, hats, or hair covering your face", R.drawable.scan_guide2)
                InstructionItem( "Avoid poor lighting","Avoid shadows or dim light on your face", R.drawable.scan_guide3)
                InstructionItem( "Don't tilt your head","Keep your head straight, don't tilt it", R.drawable.scan_guide4)
                Spacer(modifier = Modifier.height(minDimension * 0.05f))
                Button(onClick = onDismiss, modifier = Modifier.width(minDimension * 0.4f).height(minDimension * 0.12f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF04B63)), shape = RoundedCornerShape(8.dp)) {
                    Text("Start", fontSize = baseFontSize * 0.7f, fontWeight = FontWeight.Bold, color = White)
                }
            }
        }
    }
}

@Composable
fun InstructionItem(title: String, description: String, imageRes: Int) {
    val configuration = LocalConfiguration.current
    val minDimension = if (configuration.screenWidthDp < configuration.screenHeightDp) configuration.screenWidthDp.dp else configuration.screenHeightDp.dp
    val fontSize = (minDimension.value * 0.05f).sp
    val imgSize = minDimension * 0.15f

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = minDimension * 0.025f), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(imgSize)) {
            Card(shape = RoundedCornerShape(10.dp), modifier = Modifier.size(imgSize * 0.95f).align(Alignment.BottomStart)) {
                Image(painter = painterResource(id = imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
            Surface(shape = RoundedCornerShape(4.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(0.5.dp, Grey40), modifier = Modifier.size(imgSize * 0.3f).align(Alignment.TopEnd)) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Primary, modifier = Modifier.padding(2.dp))
            }
        }
        Spacer(modifier = Modifier.width(minDimension * 0.025f))
        Column {
            Text(text = title, color = Black10, fontFamily = figtreeFontFamily, fontWeight = FontWeight.Medium, fontSize = fontSize * 0.65f, lineHeight = fontSize * 0.8f,  letterSpacing = fontSize * 0.02f, modifier = Modifier.padding(start=0.4.dp))
            Spacer(modifier =  Modifier.width(minDimension * 0.04f))
            Text(text = description, fontSize = fontSize * 0.6f, color = Grey40.copy(alpha = 0.9f), lineHeight = fontSize * 0.7f, modifier = Modifier.padding(start=1.dp))
        }
    }
}

private val faceDetector = FaceDetection.getClient(
    FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .build()
)

/**
 * ON-DEVICE FACE DETECTION LOGIC
 * Uses Google ML Kit to identify face presence within camera frames.
 * Upon successful detection, converts frame to file for server-side classification.
 */
@OptIn(ExperimentalGetImage::class)
fun processFaceDetection(imageProxy: ImageProxy, viewModel: ScanViewModel, context: Context) {
    val mediaImage = imageProxy.image ?: return imageProxy.close()
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    val mlStart = System.currentTimeMillis()
    faceDetector.process(image)
        .addOnSuccessListener { faces ->
            val mlEnd = System.currentTimeMillis()

            // TIMER 2: Local performance metric for thesis evaluation
            Log.d("PERFORMANCE_TEST", ">>> Local Feature Extraction: ${mlEnd - mlStart} ms")

            if (faces.isNotEmpty()) {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val bitmap = imageProxy.toBitmap()
                        imageProxy.close()

                        val file = File(context.cacheDir, "face_scan.jpg")
                        file.outputStream().use { out ->
                            // Image compression to reduce network payload and optimize Timer 3
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 35, out)
                        }
                        viewModel.onFaceDetected(file)
                    } catch (e: Exception) {
                        Log.e("SCAN_ERROR", "Save failed: ${e.message}")
                        imageProxy.close()
                    }
                }
            } else {
                viewModel.onFaceLost()
                imageProxy.close()
            }
        }
        .addOnFailureListener {
            imageProxy.close()
        }
}