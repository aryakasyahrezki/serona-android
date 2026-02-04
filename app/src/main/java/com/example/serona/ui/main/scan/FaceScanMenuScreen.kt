package com.example.serona.ui.main.scan

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.serona.R
import com.example.serona.theme.BgGrad
import com.example.serona.theme.Grey40
import com.example.serona.theme.MutedLight
import com.example.serona.theme.Primary
import com.example.serona.theme.Secondary
import com.example.serona.theme.Secondary90
import com.example.serona.theme.WarmSoftCoral
import com.example.serona.theme.White
import com.example.serona.theme.figtreeFontFamily
import com.example.serona.ui.component.BackButton
import com.example.serona.ui.navigation.Routes
import com.example.serona.utils.FileUtils

/**
 * Entry point for the face analysis feature.
 * Provides options to either use the live camera or upload an image from the gallery.
 */
@Composable
fun FaceScanMenuScreen(
    navController: NavController,
    viewModel: FaceScanMenuViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    /** * RESPONSIVE CONFIGURATION
     * Calculates UI element sizes dynamically based on screen dimensions to prevent
     * overflow or disproportionate scaling on varied device configurations.
     */
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp
    val minDimension = if (maxWidth < maxHeight) maxWidth else maxHeight

    val titleSize = (minDimension.value * 0.06f).sp
    val descSize = (minDimension.value * 0.035f).sp
    val buttonTextSize = (minDimension.value * 0.04f).sp
    val circleSize = minDimension * 0.55f
    val fontSize = (maxWidth * 0.07f).value.sp
    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.055f
    val buttonSize = maxWidth * 0.07f
    val space = maxHeight * 0.05f

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    /**
     * CAMERA PERMISSION HANDLER
     * Ensures mandatory hardware permissions are granted before navigating to the camera interface.
     */
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) navController.navigate(Routes.SCAN)
        else Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
    }

    /**
     * GALLERY SELECTION HANDLER
     * Processes selected image URI and initiates server-side analysis via ViewModel.
     */
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val file = FileUtils.uriToFile(it, context)
            viewModel.uploadAndAnalyzeImage(file, navController)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = BgGrad)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horiPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(maxHeight * 0.2f))

            /** * DECORATIVE VISUAL ELEMENTS
             * Nested circles with Coral tones to represent focal point for scanning.
             */
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(WarmSoftCoral.copy(alpha = 0.28f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(circleSize * 0.75f)
                        .background(WarmSoftCoral.copy(alpha = 0.28f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(circleSize * 0.5f)
                            .background(WarmSoftCoral.copy(alpha = 0.38f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ar_on_you),
                            contentDescription = null,
                            modifier = Modifier.size(minDimension * 0.15f),
                            colorFilter = ColorFilter.tint(Secondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Face Recognition",
                fontSize = titleSize,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Scan your face to discover your face shape and skin tone",
                fontSize = descSize,
                fontFamily = figtreeFontFamily,
                textAlign = TextAlign.Center,
                color = MutedLight,
                modifier = Modifier.padding(horizontal = 20.dp),
                lineHeight = descSize * 1.3f
            )

            Spacer(modifier = Modifier.height(maxHeight * 0.035f))

            Column(
                modifier = Modifier.fillMaxWidth(if (maxWidth > maxHeight) 0.6f else 1f).padding(horizontal = horiPadding * 0.5f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            navController.navigate(Routes.SCAN)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Scan Now", fontSize = buttonTextSize, fontWeight = FontWeight.Bold, color = White)
                }

                Button(
                    onClick = { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Upload from Gallery", fontSize = buttonTextSize, fontWeight = FontWeight.Bold, color = White)
                }
            }

            Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horiPadding, vertical = vertiPadding)
        ) {
            Spacer(modifier = Modifier.height(space * 0.15f))

            BackButton(
                onBackClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                buttonSize = buttonSize,
                fontSize = fontSize * 0.86
            )
        }

        /**
         * LOADING OVERLAY
         * Modal overlay displayed during image processing/uploading to prevent user interaction.
         */
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Analyzing your beauty...", color = Color.White, fontFamily = figtreeFontFamily)
                }
            }
        }

        /**
         * ERROR HANDLING DIALOG
         * Informs the user about network or analysis failures with an option to retry.
         */
        if (errorMessage != null) {
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("Try Again", color = Primary, fontWeight = FontWeight.Bold, fontFamily = figtreeFontFamily)
                    }
                },
                title = {
                    Text(
                        text = "Analysis Failed",
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = titleSize * 0.8f,
                        color = Primary
                    )
                },
                text = {
                    Text(
                        text = errorMessage ?: "",
                        fontFamily = figtreeFontFamily,
                        fontSize = descSize,
                        color = Grey40
                    )
                },
                containerColor = White,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}