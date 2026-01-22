package com.example.serona.ui.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.serona.R
import com.example.serona.theme.BgGrad
import com.example.serona.ui.component.ProfileInfoItem
import com.example.serona.ui.component.ProfileMenuItem
import com.example.serona.theme.Heading
import com.example.serona.theme.OnSecondaryContainer
import com.example.serona.theme.Primary
import com.example.serona.theme.White
import com.example.serona.theme.backButtonGrad
import com.example.serona.theme.figtreeFontFamily
import com.example.serona.ui.auth.AuthViewModel
import com.example.serona.ui.component.BackButton
import com.example.serona.ui.navigation.Routes

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

    val user by profileViewModel.user.collectAsState()
    var logoutDialogBox by remember { mutableStateOf(false) }

    val name = user?.name ?: "Loading..."
    val email = user?.email ?: "..."
    val birthDate = user?.birthDate ?: "-"
    val country = user?.country ?: "-"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = BgGrad
            )
    ) {
        val configuration = LocalConfiguration.current
        val maxWidth = configuration.screenWidthDp.dp
        val maxHeight = configuration.screenHeightDp.dp

        val menuWidth = maxWidth * 0.8f
        val fontSize = (maxWidth * 0.07f).value.sp
        val buttonSize = maxWidth * 0.07f
        val profileSize = maxWidth * 0.35f
        val space = maxHeight * 0.05f

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 50.dp)
        ) {

            Spacer(modifier = Modifier.height(space * 0.15f))

            // Back Button
            BackButton(
                onBackClick = { onBackClick() },
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
                        .padding(3.5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_pic_female),
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
                    onClick = onEditProfile,
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
                    onClick = onPrivacyClick,
                    modifier = Modifier.width(menuWidth),
                    fontSize = fontSize * 0.6
                )

                ProfileMenuItem(
                    text = "Logout",
                    onClick = {logoutDialogBox = true},
                    modifier = Modifier.width(menuWidth),
                    fontSize = fontSize * 0.6
                )

                ProfileMenuItem(
                    text = "Delete Account",
                    onClick = onDeleteAccountClick,
                    modifier = Modifier.width(menuWidth),
                    fontSize = fontSize * 0.6
                )
            }
        }
    }

    if (logoutDialogBox) {
        LogoutAlertDialog (
            onConfirm = {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo(Routes.SPLASH) {
                        inclusive = true
                    }
                }
            },
            onDismiss = { logoutDialogBox = false }
        )
    }
}

@Composable
fun LogoutAlertDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
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
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout_image),
                    contentDescription = null,
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Are you going to log out of your account?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = figtreeFontFamily,
                    textAlign = TextAlign.Center,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You can log back in at any time. Don't worry.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Heading,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(140.dp)
                        .height(35.dp)
                ) {
                    Text(
                        text = "Logout",
                        color = White,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Cancel",
                    color = Primary,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onDismiss() }
                )
            }
        }
    }
}

