package com.serona.app.ui.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.serona.app.R
import com.serona.app.data.model.Gender
import com.serona.app.theme.BgGrad
import com.serona.app.ui.component.ProfileInfoItem
import com.serona.app.ui.component.ProfileMenuItem
import com.serona.app.theme.Heading
import com.serona.app.theme.OnSecondaryContainer
import com.serona.app.theme.Primary
import com.serona.app.theme.White
import com.serona.app.theme.figtreeFontFamily
import com.serona.app.ui.auth.AuthViewModel
import com.serona.app.ui.component.BackButton
import com.serona.app.utils.rememberNavigationGuard

@Composable
fun ProfilePage(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val menuWidth = maxWidth * 0.8f
    val fontSize = (maxWidth * 0.07f).value.sp
    val buttonSize = maxWidth * 0.07f
    val profileSize = maxWidth * 0.35f
    val space = maxHeight * 0.05f
    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.055f

    val user by profileViewModel.user.collectAsState(initial = null)
    var logoutDialogBox by remember { mutableStateOf(false) }

    val name = user?.name ?: "Loading..."
    val email = user?.email ?: "..."
    val birthDate = user?.birthDate ?: "-"
    val country = user?.country ?: "-"

    val (isNavigating, safeAction, resetNavigation) = rememberNavigationGuard()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = BgGrad
                )
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = horiPadding, vertical = vertiPadding)
            ) {

                Spacer(modifier = Modifier.height(space * 0.15f))

                // Back Button
                BackButton(
                    onBackClick = { safeAction { onBackClick() } },
                    buttonSize = buttonSize,
                    fontSize = fontSize * 0.86
                )

                Spacer(modifier = Modifier.height(space))

                // Profile Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(profileSize)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = Primary,
                                shape = CircleShape
                            )
                            .padding(profileSize * 0.03f)
                    ) {
                        Image(
                            painter = painterResource(id = if (user?.gender == Gender.FEMALE) R.drawable.profile_pic_female else R.drawable.profile_pic_male),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(space * 0.15f))

                    // Name
                    Text(
                        text = name,
                        fontSize = fontSize,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Heading
                    )

                    Spacer(modifier = Modifier.height(space * 0.08f))

                    // Email
                    Text(
                        text = email,
                        fontSize = fontSize * 0.6,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(space * 0.25f))

                    // Edit Profile Button
                    Button(
                        onClick = { safeAction { onEditProfile() } },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Edit Profile",
                            color = White,
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = fontSize * 0.5
                        )
                    }
                }

                Spacer(modifier = Modifier.height(space * 0.8f))

                // Info Section
                ProfileInfoItem(
                    title = "Birth of Date",
                    value = birthDate,
                    fontSize = fontSize * 0.6
                )

                ProfileInfoItem(
                    title = "Country",
                    value = country,
                    fontSize = fontSize * 0.6
                )

                Spacer(modifier = Modifier.height(space * 0.4f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileMenuItem(
                        text = "Privacy Settings",
                        onClick = { safeAction { onPrivacyClick() } },
                        modifier = Modifier.width(menuWidth),
                        fontSize = fontSize * 0.6
                    )

                    ProfileMenuItem(
                        text = "Logout",
                        onClick = { safeAction { logoutDialogBox = true } },
                        modifier = Modifier.width(menuWidth),
                        fontSize = fontSize * 0.6
                    )

                    ProfileMenuItem(
                        text = "Delete Account",
                        onClick = { safeAction { onDeleteAccountClick() } },
                        modifier = Modifier.width(menuWidth),
                        fontSize = fontSize * 0.6
                    )
                }
            }
        }

        if (isNavigating) {
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

    if (logoutDialogBox) {
        LogoutAlertDialog (
            onConfirm = {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onDismiss = {
                logoutDialogBox = false
                resetNavigation()
            },
            fontSize = fontSize,
            space = space,
            horiPadding = horiPadding,
            vertiPadding = vertiPadding,
            imageHeight = maxHeight * 0.2f
        )
    }
}

@Composable
fun LogoutAlertDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    fontSize: TextUnit,
    space: Dp,
    horiPadding: Dp,
    vertiPadding: Dp,
    imageHeight: Dp
) {
    Dialog (onDismissRequest = onDismiss) {
        Card (
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = vertiPadding, horizontal = horiPadding)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout_image),
                    contentDescription = null,
                    modifier = Modifier
                        .height(imageHeight)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(space * 0.5f))

                Text(
                    text = "Are you going to log out of your account?",
                    fontSize = fontSize * 0.55f,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = figtreeFontFamily,
                    textAlign = TextAlign.Center,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(space * 0.3f))

                Text(
                    text = "You can log back in at any time. Don't worry.",
                    fontSize = fontSize * 0.45f,
                    textAlign = TextAlign.Center,
                    color = Heading,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(space * 0.7f))

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height((fontSize * 1.3f).value.dp)
                ) {
                    Text(
                        text = "Logout",
                        color = White,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize * 0.53f
                    )
                }

                Spacer(modifier = Modifier.height(space * 0.4f))

                Text(
                    text = "Cancel",
                    color = Primary,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize * 0.53f,
                    modifier = Modifier.clickable { onDismiss() }
                )
            }
        }
    }
}

