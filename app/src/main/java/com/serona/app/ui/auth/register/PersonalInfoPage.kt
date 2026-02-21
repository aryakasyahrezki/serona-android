package com.serona.app.ui.auth.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.serona.app.theme.White
import com.serona.app.ui.component.GenderCard
import androidx.navigation.NavController
import com.serona.app.data.model.Gender
import com.serona.app.theme.Heading
import com.serona.app.theme.MutedLight
import com.serona.app.theme.Primary
import com.serona.app.theme.Primary50
import com.serona.app.theme.figtreeFontFamily
import com.serona.app.ui.auth.AuthViewModel
import com.serona.app.ui.auth.login.LoginViewModel
import com.serona.app.ui.component.CleanLinearProgress
import com.serona.app.ui.component.PersonalInfoTextField
import com.serona.app.ui.navigation.Routes
import com.serona.app.utils.ResponsiveScale
import com.serona.app.utils.rememberNavigationGuard

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PersonalInfoPage(
    navController: NavController,
    viewModel: PersonalInfoViewModel = hiltViewModel(),
    loginViewModel : LoginViewModel = hiltViewModel(),
    authViewModel : AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val progress = state.answeredCount / 3f
    val context = LocalContext.current

    val (isNavigating, safeAction, resetNavigation) = rememberNavigationGuard()

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
            resetNavigation()
        }
    }

    LaunchedEffect(state.errorMessage, state.dobError) {
        if (state.errorMessage != null || state.dobError != null) {
            if (state.errorMessage != null) {
                Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
            resetNavigation()
        }
    }

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.045f).value.sp
    val calculatedLabelSize = (fontSize.value * 0.73f).sp
    val horiPadding = maxWidth * 0.04f
    val space = maxHeight * 0.07f
    val buttonHeight = (fontSize * 2.5f).value.dp

    ResponsiveScale(maxFontScale = 1f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horiPadding)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(space))

                CleanLinearProgress(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    maxHeight = maxHeight
                )

                Spacer(modifier = Modifier.height(space * 0.5f))

                Text(
                    text = "Personal Information",
                    fontSize = fontSize,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Complete your personal information so we can tailor your beauty journey for you",
                    fontSize = fontSize * 0.75f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = MutedLight
                )

                Spacer(modifier = Modifier.height(space * 0.4f))

                Text(
                    text = "Gender",
                    fontSize = fontSize * 0.75f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(space * 0.1f))

                // Gender Selection
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GenderCard(
                        text = "Female",
                        icon = Icons.Filled.Female,
                        selected = state.gender == Gender.FEMALE,
                        onClick = { viewModel.selectGender(Gender.FEMALE) },
                        modifier = Modifier.weight(0.5f),
                        iconModifier = Modifier.rotate(45f),
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                        fontSize = fontSize
                    )

                    GenderCard(
                        text = "Male",
                        icon = Icons.Filled.Male,
                        selected = state.gender == Gender.MALE,
                        onClick = { viewModel.selectGender(Gender.MALE) },
                        modifier = Modifier.weight(0.5f),
                        maxWidth = maxWidth,
                        maxHeight = maxHeight,
                        fontSize = fontSize
                    )
                }

                Spacer(modifier = Modifier.height(space * 0.5f))

                Text(
                    text = "Country",
                    fontSize = fontSize * 0.75f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(space * 0.1f))

                // COUNTRY DROPDOWN
                PersonalInfoTextField(
                    label = "Choose Your Country",
                    value = state.country,
                    onValueChange = { },
                    isDropdown = true,
                    dropdownItems = listOf(
                        "China",
                        "Germany",
                        "Indonesia",
                        "Japan",
                        "Malaysia",
                        "Singapore",
                        "South Africa",
                        "South Korea",
                        "Thailand",
                        "United Kingdom",
                        "United States",
                        "North Korea"
                    ),
                    onDropdownItemSelected = { viewModel.selectCountry(it) },
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = fontSize,
                    labelFontSize = calculatedLabelSize,
                    maxWidth = maxWidth,
                    maxHeight = maxHeight
                )

                Spacer(modifier = Modifier.height(space * 0.5f))

                Text(
                    text = "Date of Birth",
                    fontSize = fontSize * 0.75f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(space * 0.008f))

                // DATE OF BIRTH
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(maxWidth * 0.007f)
                ) {

                    PersonalInfoTextField(
                        label = "Day",
                        value = state.day,
                        onValueChange = { },
                        isDropdown = true,
                        dropdownItems = (1..31).map { it.toString() },
                        onDropdownItemSelected = { day ->
                            viewModel.selectDOB(day, state.month, state.year)
                        },
                        modifier = Modifier.weight(0.95f),
                        fontSize = fontSize,
                        labelFontSize = calculatedLabelSize,
                        maxWidth = maxWidth,
                        maxHeight = maxHeight
                    )

                    PersonalInfoTextField(
                        label = "Month",
                        value = state.month,
                        onValueChange = { },
                        isDropdown = true,
                        dropdownItems = listOf(
                            "Jan",
                            "Feb",
                            "Mar",
                            "Apr",
                            "May",
                            "Jun",
                            "Jul",
                            "Aug",
                            "Sept",
                            "Oct",
                            "Nov",
                            "Dec"
                        ),
                        onDropdownItemSelected = { month ->
                            viewModel.selectDOB(state.day, month, state.year)
                        },
                        modifier = Modifier.weight(1.1f),
                        fontSize = fontSize,
                        labelFontSize = calculatedLabelSize,
                        maxWidth = maxWidth,
                        maxHeight = maxHeight
                    )

                    PersonalInfoTextField(
                        label = "Year",
                        value = state.year,
                        onValueChange = { },
                        isDropdown = true,
                        dropdownItems = (1960..2015).map { it.toString() },
                        onDropdownItemSelected = { year ->
                            viewModel.selectDOB(state.day, state.month, year)
                        },
                        modifier = Modifier.weight(0.97f),
                        fontSize = fontSize,
                        labelFontSize = calculatedLabelSize,
                        maxWidth = maxWidth,
                        maxHeight = maxHeight
                    )

                }


                if (state.dobError != null) {
                    Text(
                        text = state.dobError!!,
                        color = Primary50,
                        fontSize = fontSize * 0.6f,
                        fontFamily = figtreeFontFamily,
                        modifier = Modifier.padding(start = space * 0.08f)
                    )
                }

                Spacer(modifier = Modifier.height(space * 0.8f))

                // NEXT BUTTON
                Button(
                    onClick = {
                        safeAction {
                            val isValid = viewModel.submitPersonalInfo {
                                authViewModel.checkAuthStatus()

                                navController.navigate("home") {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }

                            if (!isValid) {
                                resetNavigation()
                            }
                        }
                    },
                    enabled = state.canContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(buttonHeight),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE15B6F),
                        disabledContainerColor = Color(0xFFE5AEB4)
                    )
                ) {
                    Text(
                        text = "Next",
                        color = Color.White,
                        fontSize = fontSize * 0.8f,
                        fontFamily = figtreeFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(space * 0.5f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Back to Login ? ",
                        color = MutedLight,
                        fontSize = fontSize * 0.6f,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Login",
                        color = Primary,
                        fontSize = fontSize * 0.6f,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            safeAction {
                                navController.navigate(Routes.LOGIN) {
                                    popUpTo(Routes.LOGIN) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                                loginViewModel.resetLoginState()
                            }
                        }
                    )
                }
            }

            if (isNavigating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(100f) // Pastikan di depan dropdown dan input
                        .background(Color.Transparent)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                }
                            }
                        }
                )
            }
        }
    }
}