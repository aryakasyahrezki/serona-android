package com.example.serona.ui.main.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.serona.theme.BgGrad
import com.example.serona.theme.Heading
import com.example.serona.theme.Primary
import com.example.serona.theme.Tertiary
import com.example.serona.theme.White
import com.example.serona.theme.figtreeFontFamily

@Composable
fun DeleteAccPage(
    viewModel: DeleteAccViewModel = hiltViewModel(),
    onDeleteConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.055f
    val fontSize = (maxWidth * 0.065f).value.sp
    val iconSize = maxWidth * 0.15f
    val space = maxHeight * 0.05f

    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Handle Error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = BgGrad)
            .padding(horizontal = horiPadding, vertical = vertiPadding),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = horiPadding * 0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Delete Account",
                color = Tertiary,
                fontSize = fontSize,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(space * 1.8f))

            Box(
                modifier = Modifier
                    .size(iconSize * 2.3f)
                    .background(
                        color = Primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(iconSize)
                )
            }

            Spacer(modifier = Modifier.height(space * 1.8f))

            Text(
                text = "Are you sure ?",
                fontSize = fontSize,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Tertiary
            )

            Spacer(modifier = Modifier.height(space * 0.5f))

            Text(
                text = "If you delete your account, all your data, such as your profile, preferences, and usage history, will be permanently removed.",
                fontSize = fontSize * 0.58f,
                color = Heading,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(space * 0.5f))

            Text(
                text = "Are you sure you want to delete your account?",
                fontSize = fontSize * 0.58f,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(space * 1.3f))

            Button(
                onClick = { showDialog = true },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight * 0.055f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isLoading) "Deleting..." else "Delete Account",
                    color = Color.White,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize * 0.65f
                )
            }

            Spacer(modifier = Modifier.height(space * 0.5f))

            Text(
                text = "Cancel",
                color = Primary,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize * 0.65f,
                modifier = Modifier
                    .clickable { onCancel() }
            )
        }

        if (showDialog) {
            DeleteAccountDialog(
                onConfirm = { inputPassword ->
                    // Memanggil fungsi delete yang butuh password
                    viewModel.deleteAccount(inputPassword, onDeleteConfirm)
                    showDialog = false
                },
                onDismiss = { showDialog = false },
                isLoading = isLoading,
                fontSize = fontSize,
                space = space
            )
        }
    }
}

@Composable
fun DeleteAccountDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean,
    fontSize: TextUnit,
    space: Dp
) {
    var password by remember { mutableStateOf("") }

    val buttonHeight = (fontSize * 1.5f).value.dp

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = White,
            modifier = Modifier.fillMaxWidth(0.95f).wrapContentHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(space * 0.5f)
            ) {

                Text(
                    text = "Confirm Password",
                    fontSize = fontSize * 0.7f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(space * 0.3f))

                Text(
                    text = "Please enter your password to permanently delete your account.",
                    fontSize = fontSize * 0.5f,
                    color = Color.Gray,
                    fontFamily = figtreeFontFamily,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(space * 0.5f))

                // --- Input Password ---
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Enter your password", fontFamily = figtreeFontFamily, fontSize = fontSize * 0.6f) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(), // Sensor password
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(space * 0.8f))

                Button(
                    onClick = { onConfirm(password) },
                    enabled = !isLoading && password.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(buttonHeight)
                ) {
                    Text(
                        text = if (isLoading) "Processing..." else "Delete Permanently",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize * 0.6f,
                        fontFamily = figtreeFontFamily
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text(
                        "Cancel",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize * 0.6f,
                        fontFamily = figtreeFontFamily
                    )
                }
            }
        }
    }
}