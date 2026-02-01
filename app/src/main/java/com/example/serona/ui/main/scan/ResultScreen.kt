package com.example.serona.ui.main.scan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.serona.R
import com.example.serona.theme.*
import com.example.serona.ui.navigation.Routes
import java.net.URLDecoder

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: ResultViewModel = hiltViewModel()
) {
    // 1. KONFIGURASI RESPONSIF
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    // Trik minDimension: Supaya font/jarak tidak meledak saat Landscape
    val minDimension = if (maxWidth < maxHeight) maxWidth else maxHeight
    val baseFontSize = (minDimension.value * 0.05f).sp

    // Tinggi tombol yang dikunci (Min 48dp - Max 60dp) agar tidak gepeng saat miring
    val buttonHeight = (maxHeight * 0.07f).coerceIn(48.dp, 60.dp)

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.saveSuccess, state.errorMessage) {
        state.errorMessage?.let { msg ->
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    // Ambil data yang sudah bersih dari ViewModel
    val decodedShape = viewModel.decodedShape
    val decodedTone = viewModel.decodedTone

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F1))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = maxWidth * 0.06f, vertical = maxHeight * 0.02f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER
        Text(
            text = "Beauty Profile",
            fontSize = baseFontSize * 1.2f,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF04B63),
            fontFamily = figtreeFontFamily
        )

        Spacer(modifier = Modifier.height(maxHeight * 0.02f))

        // AVATAR DISPLAY
        Box(
            modifier = Modifier
                .size(minDimension * 0.4f) // Mengikuti sisi terpendek agar tetap bulat
                .background(White, RoundedCornerShape(minDimension * 0.05f))
                .padding(minDimension * 0.025f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = getAvatarByResult(decodedShape, decodedTone)),
                contentDescription = "Your Face Avatar",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(maxHeight * 0.025f))

        // RESULT DETAILS CARD
        Card(
            // Saat miring (landscape), lebar kartu dibatasi 70% agar teks enak dibaca
            modifier = Modifier.fillMaxWidth(if (maxWidth > maxHeight) 0.7f else 1f),
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape(minDimension * 0.04f),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(minDimension * 0.05f, minDimension * 0.035f)) {
                Text(
                    text = "Personalized Analysis",
                    fontSize = baseFontSize * 0.9f,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFF04B63),
                    fontFamily = figtreeFontFamily
                )

                Spacer(modifier = Modifier.height(8.dp))

                DetailItem(
                    label = "Face Shape",
                    value = decodedShape,
                    description = getFaceDescription(decodedShape),
                    baseFontSize = baseFontSize
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 0.5.dp,
                    color = MutedLight
                )

                DetailItem(
                    label = "Skin Tone",
                    value = decodedTone,
                    description = getSkinToneDescription(decodedTone),
                    baseFontSize = baseFontSize
                )
            }
        }

        Spacer(modifier = Modifier.height(maxHeight * 0.04f))

        // ACTION BUTTONS
        Column(
            modifier = Modifier.fillMaxWidth(if (maxWidth > maxHeight) 0.6f else 1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { /* Navigasi ke Rekomendasi */ },
                modifier = Modifier.fillMaxWidth().height(buttonHeight * 0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("See Recommendation", fontSize = baseFontSize * 0.8f, fontWeight = FontWeight.Bold, color = White)
            }



            OutlinedButton(
                onClick = { viewModel.saveToProfile() },
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth().height(buttonHeight * 0.8f),
                border = BorderStroke(1.5.dp, Primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save to Profile", fontSize = baseFontSize * 0.8f, fontWeight = FontWeight.Bold, color = Primary)
            }

            TextButton(
                onClick = {
                    navController.navigate(Routes.SCAN_MENU) {
                        popUpTo(Routes.SCAN_MENU) { inclusive = true }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).height(buttonHeight * 0.8f),
            ) {
                Text(
                    text = "Scan Again",
                    fontSize = baseFontSize * 0.7f,
                    color = Grey40,
                    fontFamily = figtreeFontFamily
                )
            }
        }

        Spacer(modifier = Modifier
            .navigationBarsPadding() // Ini kuncinya! Ngikutin settingan HP user
            .padding(bottom = 16.dp) // Tambahan jarak sedikit supaya nggak nempel banget
        )
    }
}

@Composable
fun DetailItem(
    label: String,
    value: String,
    description: String,
    baseFontSize: androidx.compose.ui.unit.TextUnit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = baseFontSize * 0.6f,
            color = Grey40.copy(alpha = 0.7f),
            fontFamily = figtreeFontFamily,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )

        Text(
            text = value,
            fontSize = baseFontSize * 0.8f,
            fontWeight = FontWeight.Bold,
            color = Black10,
            fontFamily = figtreeFontFamily
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = description,
            fontSize = baseFontSize * 0.68f,
            color = Grey40,
            lineHeight = baseFontSize * 1.1f, // Spasi antar baris proporsional
            fontFamily = figtreeFontFamily,
            textAlign = androidx.compose.ui.text.style.TextAlign.Justify
        )
    }
}

// --- FUNGSI PENDUKUNG (DATA) ---

@Composable
fun getAvatarByResult(shape: String, tone: String): Int {
    return when {
        shape.contains("Oval", ignoreCase = true) -> {
            when {
                tone.contains("Fair", ignoreCase = true) -> R.drawable.fair_oval
                tone.contains("Dark", ignoreCase = true) -> R.drawable.dark_oval
                else -> R.drawable.medium_oval
            }
        }
        shape.contains("Round", ignoreCase = true) -> {
            when {
                tone.contains("Fair", ignoreCase = true) -> R.drawable.fair_round
                tone.contains("Dark", ignoreCase = true) -> R.drawable.dark_round
                else -> R.drawable.medium_round
            }
        }
        shape.contains("Square", ignoreCase = true) -> {
            when {
                tone.contains("Fair", ignoreCase = true) -> R.drawable.fair_square
                tone.contains("Dark", ignoreCase = true) -> R.drawable.dark_square
                else -> R.drawable.medium_square
            }
        }
        shape.contains("Heart", ignoreCase = true) -> {
            when {
                tone.contains("Fair", ignoreCase = true) -> R.drawable.fair_heart
                tone.contains("Dark", ignoreCase = true) -> R.drawable.dark_heart
                else -> R.drawable.medium_heart
            }
        }
        shape.contains("Oblong", ignoreCase = true) -> {
            when {
                tone.contains("Fair", ignoreCase = true) -> R.drawable.fair_oblong
                tone.contains("Dark", ignoreCase = true) -> R.drawable.dark_oblong
                else -> R.drawable.medium_oblong
            }
        }
        else -> R.drawable.medium_oval
    }
}

fun getFaceDescription(shape: String): String {
    return when {
        shape.contains("Oval", ignoreCase = true) ->
            "Your face features a highly balanced and symmetrical structure. This harmonious proportion is widely considered a versatile canvas that elegantly complements almost any style."
        shape.contains("Square", ignoreCase = true) ->
            "You have a strong jawline and a broad forehead, projecting a sense of confidence and authority. Your prominent bone structure gives your features an iconic and dignified look."
        shape.contains("Round", ignoreCase = true) ->
            "Soft, curved lines without sharp angles provide you with a naturally friendly and fresh appearance. This structure often radiates a welcoming and youthful glow."
        shape.contains("Heart", ignoreCase = true) ->
            "Your high cheekbones and elegantly tapered chin draw natural attention to your eyes. This sophisticated structure highlights your facial features with a graceful touch."
        shape.contains("Oblong", ignoreCase = true) ->
            "A slender and elongated bone structure creates a sleek, sophisticated profile. This refined appearance gives you a naturally chic and stylish vibe."
        else -> "You possess a unique and intriguing facial structure that radiates personal charm."
    }
}

fun getSkinToneDescription(tone: String): String {
    return when {
        tone.contains("Fair", ignoreCase = true) || tone.contains("Light", ignoreCase = true) ->
            "Your skin possesses a natural clarity that provides a beautiful contrast to your features. It often radiates a clear, bright, and translucent healthy glow."
        tone.contains("Medium", ignoreCase = true) || tone.contains("Tan", ignoreCase = true) ->
            "You have a warm, sun-kissed hue that looks exotic and healthy. This skin tone often has a natural shimmer that exudes a vibrant and positive energy."
        tone.contains("Dark", ignoreCase = true) ->
            "Your skin has an elegant and rich depth of color that sharpens your facial features. This complexion appears remarkably smooth with a profound natural appeal."
        else -> "Your skin tone has a beautiful and unique radiance that enhances your natural beauty."
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_6A)
@Composable
fun ResultScreenPreview() {
//    ResultScreen(navController = rememberNavController(), shape = "Round", tone = "Medium Tan")
}