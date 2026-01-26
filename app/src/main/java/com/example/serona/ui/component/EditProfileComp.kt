package com.example.serona.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.serona.theme.Heading
import com.example.serona.theme.MutedLight
import com.example.serona.theme.Primary
import com.example.serona.theme.Primary50
import com.example.serona.theme.White
import com.example.serona.theme.figtreeFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileField(
    modifier: Modifier = Modifier,
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorText: String? = null,
    isDropdown: Boolean = false,
    dropdownItems: List<String> = emptyList(),
    onDropdownItemSelected: (String) -> Unit = {},
    fontSize: TextUnit = 12.sp,
) {
    var expanded by remember { mutableStateOf(false) }

    var textFieldWidth by remember { mutableStateOf(0) }
    var textFieldHeight by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    Column(modifier = modifier) {

        // Title
        Text(
            text = title,
            fontSize = fontSize,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Medium,
            color = Heading
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded && isDropdown,
            onExpandedChange = {
                if (isDropdown) expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (!isDropdown) onValueChange(it)
                },
                readOnly = isDropdown,
                singleLine = true,

                placeholder = {
                    Text(
                        text = placeholder,
                        fontSize = fontSize,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = MutedLight
                    )
                },

                trailingIcon = {
                    if (isDropdown) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    }
                },

                isError = errorText != null,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Primary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .heightIn(min = 44.dp)
                    .onSizeChanged {
                        // Simpan lebar TextField saat ukurannya berubah
                        textFieldWidth = it.width
                        textFieldHeight = it.height
                    },
                shape = RoundedCornerShape(15.dp),
            )


            if (expanded) {
                Popup(
                    alignment = Alignment.TopEnd,
                    offset = IntOffset(
                        x = 0,               // Tidak digeser secara horizontal
                        y = textFieldHeight // Geser ke bawah
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
                            .heightIn(max = 200.dp)
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(15.dp))
                            .background(
                                color = White, // Warna latar belakang menu
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
                                        fontSize = fontSize * 1.2f,
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
            }
        }

        if (errorText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorText,
                color = Primary50,
                fontSize = fontSize,
                fontFamily = figtreeFontFamily,
                modifier = Modifier.padding(start = 5.dp)
            )
        }

    }
}