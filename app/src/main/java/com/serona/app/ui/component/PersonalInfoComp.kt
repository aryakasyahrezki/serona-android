package com.serona.app.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.serona.app.theme.GenderButtonGrad
import com.serona.app.theme.Tertiary20
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.serona.app.theme.Grey40
import com.serona.app.theme.MutedLight
import com.serona.app.theme.Primary
import com.serona.app.theme.Primary50
import com.serona.app.theme.White
import com.serona.app.theme.figtreeFontFamily

@Composable
fun GenderCard(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    iconModifier: Modifier = Modifier,
    maxWidth: Dp,
    maxHeight: Dp,
    fontSize: TextUnit
) {
    val iconSize = maxWidth * 0.15f
    val space = (maxHeight * 0.04f)

    Box(
        modifier = modifier   // ← weight masuk dari luar
            .height(maxHeight * 0.2f)
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
                modifier = iconModifier.size(iconSize)
            )
            Spacer(modifier = Modifier.height(space * 0.4f))
            Text(
                text = text,
                fontSize = fontSize * 0.9f,
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
    fontSize: TextUnit,
    labelFontSize: TextUnit,
    maxWidth: Dp,
    maxHeight: Dp
) {
    var expanded by remember { mutableStateOf(false) }

    var textFieldWidth by remember { mutableStateOf(0) }
    var textFieldHeight by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    if (isDropdown) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            Box(contentAlignment = Alignment.CenterStart) {
                val isCountryField = label == "Choose Your Country"

                OutlinedTextField(
                    value = value,
                    onValueChange = {},
                    readOnly = true,
                    shape = RoundedCornerShape(15.dp),
                    label = if (isCountryField) null else {
                        { Text(
                            label,
                            fontSize = fontSize * 0.7f,
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        ) }
                    },
                    textStyle = TextStyle(
                        fontSize = fontSize * 0.65f,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Grey40
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .onSizeChanged {
                            textFieldWidth = it.width
                            textFieldHeight = it.height
                        },

                    trailingIcon = {
                        if (isDropdown) {
                            androidx.compose.material3.Icon(
                                imageVector = if (expanded)
                                    androidx.compose.material.icons.Icons.Filled.KeyboardArrowUp
                                else
                                    androidx.compose.material.icons.Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MutedLight
                            )
                        }
                    },

                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Primary50.copy(alpha = 0.4f),
                        focusedIndicatorColor = Primary50.copy(alpha = 0.5f),
                        unfocusedLabelColor = Primary50.copy(alpha = 0.8f),
                        focusedLabelColor = Primary50,
                        errorContainerColor = Color.Transparent,
                    )
                )

                // INI ADALAH PLACEHOLDER/LABEL MANUAL KAMU
                if (isCountryField) {
                    AnimatedVisibility(
                        // Muncul saat kosong, HILANG saat fokus atau expand
                        visible = value.isEmpty() && !expanded,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(
                            text = label, // "Choose Your Country"
                            fontSize = fontSize * 0.7f, // Sesuaikan ukuran agar sama dengan isi
                            fontFamily = figtreeFontFamily,
                            color = Primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (expanded) {
                Popup(
                    alignment = Alignment.TopEnd,
                    offset = IntOffset(
                        x = 0,
                        y = textFieldHeight
                    ),
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(
                        focusable = true,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                        excludeFromSystemGesture = true
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .width(with(density) { textFieldWidth.toDp() })
                            .heightIn(max = maxHeight * 0.2f)
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(15.dp))
                            .background(
                                color = White,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Isi menu dengan DropdownMenuItem
                        dropdownItems.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        item,
                                        fontSize = fontSize * 0.7f,
                                        fontFamily = figtreeFontFamily,
                                        fontWeight = if (item == value) FontWeight.SemiBold else FontWeight.Normal,
                                        color = Grey40
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
    maxHeight: Dp,
    progressColor: Color = Color(0xFFF99E93),
    trackColor: Color = Color(0xFFECECEC)
) {
    val shape = RoundedCornerShape(100.dp)
    val height = maxHeight * 0.01f

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

