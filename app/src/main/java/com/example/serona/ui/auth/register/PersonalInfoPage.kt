package com.example.serona.ui.auth.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.serona.theme.White
import com.example.serona.ui.component.GenderCard
import androidx.navigation.NavController
import com.example.serona.data.model.Gender
import com.example.serona.theme.MutedLight
import com.example.serona.theme.figtreeFontFamily
import com.example.serona.ui.component.CleanLinearProgress
import com.example.serona.ui.component.PersonalInfoTextField
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PersonalInfoPage(
    navController: NavController,
    viewModel: PersonalInfoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val progress = state.answeredCount / 3f
    val context = LocalContext.current

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.052f).value.sp
    val calculatedLabelSize = (fontSize.value * 0.73f).sp
    val horiPadding = maxWidth * 0.05f
    val space = maxHeight * 0.07f
    val buttonHeight = (fontSize * 2f).value.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horiPadding)
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

            Spacer(modifier = Modifier.height(space * 0.5f))

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

            // DATE OF BIRTH
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                PersonalInfoTextField(
                    label = "Day",
                    value = state.day,
                    onValueChange = { },
                    isDropdown = true,
                    dropdownItems = (1..30).map { it.toString() },
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
                    dropdownItems = (1999..2010).map { it.toString() },
                    onDropdownItemSelected = { year ->
                        viewModel.selectDOB(state.day, state.month, year)
                    },
                    modifier = Modifier.weight(0.95f),
                    fontSize = fontSize,
                    labelFontSize = calculatedLabelSize,
                    maxWidth = maxWidth,
                    maxHeight = maxHeight
                )

            }

            Spacer(modifier = Modifier.height(space * 0.5f))

            // NEXT BUTTON
            Button(
                onClick = {
                    viewModel.submitPersonalInfo {
                        navController.navigate("home") {
                            popUpTo("personalInfo") {
                                inclusive = true
                            }
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
        }
    }
}