package com.example.serona.ui.ui.auth.register

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.serona.ui.theme.White
import com.example.serona.ui.ui.component.GenderCard
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.serona.ui.theme.MutedLight
import com.example.serona.ui.theme.figtreeFontFamily
import com.example.serona.ui.ui.component.CleanLinearProgress
import com.example.serona.ui.ui.component.PersonalInfoTextField

@Composable
fun PersonalInfoPage(
    navController: NavController,
    viewModel: PersonalInfoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val progress = state.answeredCount / 3f

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 2.dp)
            .background(color = White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            CleanLinearProgress(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Personal Information",
                fontSize = 24.sp,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Complete your personal information so we can tailor your beauty journey for you",
                fontSize = 14.sp,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = MutedLight
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                    iconModifier = Modifier.rotate(45f)
                )

                GenderCard(
                    text = "Male",
                    icon = Icons.Filled.Male,
                    selected = state.gender == Gender.MALE,
                    onClick = { viewModel.selectGender(Gender.MALE) },
                    modifier = Modifier.weight(0.5f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // COUNTRY DROPDOWN
            PersonalInfoTextField(
                label = "Choose Your Country",
                value = state.country,
                onValueChange = { },               // ignored karena isDropdown = true
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier.weight(0.95f)
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
                    modifier = Modifier.weight(1.1f)
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
                    modifier = Modifier.weight(0.95f)
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            // NEXT BUTTON
            Button(
                onClick = {
                    navController.navigate("home"){
                        popUpTo("personalInfo"){
                            inclusive = true
                        }
                    }
                },
                enabled = state.canContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE15B6F),
                    disabledContainerColor = Color(0xFFE5AEB4)
                )
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}