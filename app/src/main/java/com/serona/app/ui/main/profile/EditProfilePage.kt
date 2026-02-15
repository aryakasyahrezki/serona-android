package com.serona.app.ui.main.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serona.app.R
import com.serona.app.data.model.Gender
import com.serona.app.ui.component.EditProfileField
import com.serona.app.theme.BgGrad
import com.serona.app.theme.Primary
import com.serona.app.theme.Primary50
import com.serona.app.theme.White
import com.serona.app.theme.figtreeFontFamily
import com.serona.app.ui.component.BackButton
import com.serona.app.utils.rememberNavigationGuard

@Composable
fun EditProfilePage(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.055f
    val fontSize = (maxWidth * 0.07f).value.sp
    val buttonSize = maxWidth * 0.07f
    val space = maxHeight * 0.05f
    val avatarBoxSize = maxWidth * 0.35f

    val state by editProfileViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val (isNavigating, safeAction, resetNavigation) = rememberNavigationGuard()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            editProfileViewModel.onEvent(EditProfileEvent.ClearError)
            resetNavigation()
        }
    }

    LaunchedEffect(state.nameError, state.countryError, state.dobError) {
        if (state.nameError != null || state.countryError != null || state.dobError != null) {
            resetNavigation()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures (onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = BgGrad)
                .padding(vertical = vertiPadding, horizontal = horiPadding)
        ) {

            Column() {
                Spacer(modifier = Modifier.height(space * 0.15f))

                // Back Button
                BackButton(
                    onBackClick = { safeAction { onBackClick() } },
                    buttonSize = buttonSize,
                    fontSize = fontSize * 0.86
                )

                // Profile Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(space))

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(avatarBoxSize)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = Primary,
                                shape = CircleShape
                            )
                            .padding(avatarBoxSize * 0.03f)
                    ) {
                        Image(
                            painter = painterResource(id = if (state.gender == Gender.FEMALE) R.drawable.profile_pic_female else R.drawable.profile_pic_male),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(space * 0.7f))

                    // Name
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(space * 0.5f)
                    ) {
                        EditProfileField(
                            title = "Full Name",
                            placeholder = "Enter your new Full Name",
                            value = state.name,
                            onValueChange = {
                                editProfileViewModel.onEvent(
                                    EditProfileEvent.NameChanged(
                                        it
                                    )
                                )
                            },
                            errorText = state.nameError,
                            fontSize = fontSize,
                            space = space,
                            enabled = !isNavigating
                        )

                        EditProfileField(
                            title = "Gender",
                            placeholder = "Select your Gender",
                            value = if (state.gender == Gender.MALE) "Male" else "Female",
                            onValueChange = {},
                            isDropdown = true,
                            dropdownItems = listOf("Male", "Female"),
                            onDropdownItemSelected = { selectedString ->
                                val selectedGender =
                                    if (selectedString == "Male") Gender.MALE else Gender.FEMALE

                                editProfileViewModel.onEvent(
                                    EditProfileEvent.GenderChanged(
                                        selectedGender
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = fontSize,
                            space = space
                        )

                        EditProfileField(
                            title = "Country",
                            placeholder = "Enter your new Country",
                            value = state.country,
                            onValueChange = {},
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
                            onDropdownItemSelected = {
                                editProfileViewModel.onEvent(
                                    EditProfileEvent.CountryChanged(
                                        it
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            errorText = state.countryError,
                            fontSize = fontSize,
                            space = space
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(maxWidth * 0.009f)
                            ) {

                                EditProfileField(
                                    title = "Date of Birth",
                                    placeholder = "Day",
                                    value = state.day,
                                    onValueChange = {},
                                    isDropdown = true,
                                    dropdownItems = (1..31).map { it.toString() },
                                    onDropdownItemSelected = {
                                        editProfileViewModel.onEvent(
                                            EditProfileEvent.DayChanged(it)
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    fontSize = fontSize,
                                    space = space
                                )

                                EditProfileField(
                                    title = "",
                                    placeholder = "Month",
                                    value = state.month,
                                    onValueChange = {},
                                    isDropdown = true,
                                    dropdownItems = listOf(
                                        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                                    ),
                                    onDropdownItemSelected = {
                                        editProfileViewModel.onEvent(
                                            EditProfileEvent.MonthChanged(it)
                                        )
                                    },
                                    modifier = Modifier.weight(1.2f),
                                    fontSize = fontSize,
                                    space = space
                                )

                                EditProfileField(
                                    title = "",
                                    placeholder = "Year",
                                    value = state.year,
                                    onValueChange = {},
                                    isDropdown = true,
                                    dropdownItems = (1960..2015).map { it.toString() },
                                    onDropdownItemSelected = {
                                        editProfileViewModel.onEvent(
                                            EditProfileEvent.YearChanged(it)
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    fontSize = fontSize,
                                    space = space
                                )
                            }

                            state.dobError?.let { errorMsg ->
                                Spacer(modifier = Modifier.height(space * 0.1f))
                                Text(
                                    text = errorMsg,
                                    color = Primary50,
                                    fontSize = fontSize * 0.5f,
                                    fontFamily = figtreeFontFamily
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(space * 0.8f))

                    // Edit Profile Button
                    Button(
                        onClick = { safeAction { editProfileViewModel.onEvent(EditProfileEvent.Save) } },
                        enabled = state.isChanged && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            disabledContainerColor = Primary.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (state.isLoading) "Saving..." else "Save Changes",
                            color = White,
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        if (isNavigating || state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
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

// & "C:\Users\deaud\AppData\Local\Android\Sdk\emulator\emulator.exe" -avd Pixel_6a_2 -dns-server 8.8.8.8 -no-snapshot-load