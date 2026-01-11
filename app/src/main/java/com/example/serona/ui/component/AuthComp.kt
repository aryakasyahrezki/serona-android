package com.example.serona.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serona.theme.Primary
import com.example.serona.theme.Primary50
import com.example.serona.theme.White
import com.example.serona.theme.White10
import com.example.serona.theme.figtreeFontFamily

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    color: Color = White
) {
    Column() {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(label,
                    fontSize = 15.sp,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Medium
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = color.copy(alpha = 0.4f),
                focusedIndicatorColor =color.copy(alpha = 0.5f),
                unfocusedLabelColor = color.copy(alpha = 0.8f),
                focusedLabelColor = color,
                errorContainerColor = Color.Transparent,
                errorIndicatorColor = Primary50.copy(alpha = 0.7f),
                errorLabelColor = Primary50,
                cursorColor = Color.White
            ),
            isError = error != null,
            modifier = Modifier
                .fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error,
                color = Primary50,
                fontSize = 12.sp,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?
) {
    var visible by remember { mutableStateOf(false) }

    Column() {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(label,
                    fontSize = 15.sp,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Medium)
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector =
                            if (visible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = Color(0xFF292929)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.4f),
                focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                unfocusedLabelColor = White10.copy(alpha = 0.8f),
                focusedLabelColor = White10,
                errorContainerColor = Color.Transparent,
                errorIndicatorColor = Primary50.copy(alpha = 0.7f),
                errorLabelColor = Primary50,
                cursorColor = Color.White,
            ),
            isError = error != null,
            modifier = Modifier
                .fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error,
                color = Primary50,
                fontSize = 12.sp,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                White
            )
            .border(
                width = 1.5.dp,
                color = White,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}