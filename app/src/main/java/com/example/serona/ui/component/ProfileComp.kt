package com.example.serona.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.serona.theme.Grey30
import com.example.serona.theme.Heading
import com.example.serona.theme.OnSecondaryContainer
import com.example.serona.theme.figtreeFontFamily

@Composable
fun ProfileInfoItem(
    title: String,
    value: String,
    fontSize: TextUnit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = fontSize,
            color = Heading,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = fontSize,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Medium,
            color = OnSecondaryContainer
        )
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = Grey30
    )
}

@Composable
fun ProfileMenuItem(
    text: String,
    textColor: Color = Color.Black,
    onClick: () -> Unit,
    modifier: Modifier,
    fontSize: TextUnit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            color = textColor,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
