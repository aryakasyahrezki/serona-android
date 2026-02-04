package com.serona.app.ui.component

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.serona.app.theme.Primary
import com.serona.app.theme.Primary50
import com.serona.app.theme.White
import com.serona.app.theme.White10
import com.serona.app.theme.figtreeFontFamily

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    color: Color = White,
    fontSize: TextUnit,
    space: Dp
) {
    Column() {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(label,
                    fontSize = fontSize * 0.7f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Medium
                )
            },
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = fontSize * 0.7f,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Medium,
                color = White
            ),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = color.copy(alpha = 0.6f),
                focusedIndicatorColor =color.copy(alpha = 0.5f),
                unfocusedLabelColor = color.copy(alpha = 0.9f),
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
                fontSize = fontSize * 0.6f,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = space * 0.1f)
            )
        }
    }
}

@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    fontSize: TextUnit,
    space: Dp
) {
    var visible by remember { mutableStateOf(false) }

    Column() {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(label,
                    fontSize = fontSize * 0.7f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Medium)
            },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = fontSize * 0.7f,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Medium,
                color = White
            ),
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
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.6f),
                focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                unfocusedLabelColor = White10.copy(alpha = 0.9f),
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
                fontSize = fontSize * 0.4f,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = space * 0.08f)
            )
        }
    }
}

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    fontSize: TextUnit
) {

    val iconSize = (fontSize * 0.7f).value.dp

    Box(
        modifier = Modifier
            .size(iconSize * 1.5f)
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
                modifier = Modifier.size(iconSize)
            )
        }
    }
}