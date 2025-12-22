package com.example.serona.ui.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serona.ui.theme.GenderButtonGrad
import com.example.serona.ui.theme.Tertiary20
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.serona.ui.theme.MutedLight
import com.example.serona.ui.theme.Primary50
import com.example.serona.ui.theme.White
import com.example.serona.ui.theme.White10
import com.example.serona.ui.theme.figtreeFontFamily

@Composable
fun GenderCard(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    iconModifier: Modifier = Modifier
) {
    Box(
        modifier = modifier   // ← weight masuk dari luar
            .height(130.dp)
            .background(
                brush = if (selected) GenderButtonGrad else Brush.linearGradient(
                    listOf(
                        Color.White,
                        Color.White
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (!selected) Tertiary20 else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.White else Color(0xFFE15B6F),
                modifier = iconModifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 20.sp,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color(0xFFE15B6F)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isDropdown: Boolean = false,
    dropdownItems: List<String> = emptyList(),
    onDropdownItemSelected: (String) -> Unit = {},
    modifier: Modifier,
    labelFontSize: TextUnit = 16.sp,
    paddingValues: PaddingValues? = null
) {
    var expanded by remember { mutableStateOf(false) }

    if (isDropdown) {

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier  // weight masuk dari Row
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        label,
                        fontSize = labelFontSize,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = MutedLight
                ),
                shape = RoundedCornerShape(15.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),     // anchor langsung di textfield
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Primary50.copy(alpha = 0.4f), // Ganti sesuai tema
                    focusedIndicatorColor = Primary50.copy(alpha = 0.5f), // Ganti sesuai tema
                    unfocusedLabelColor = Primary50.copy(alpha = 0.8f),
                    focusedLabelColor = Primary50,
                    errorContainerColor = Color.Transparent,
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp))
                    .background(
                        color = White10,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                item,
                                fontSize = 16.sp,
                                fontFamily = figtreeFontFamily,
                                fontWeight = if (item == value) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onDropdownItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }

    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CleanLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    cornerRadius: Dp = 100.dp,
    progressColor: Color = Color(0xFFF99E93),
    trackColor: Color = Color(0xFFECECEC)
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(shape)
                .background(progressColor)
        )
    }
}

