package com.serona.app.ui.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.serona.app.data.model.PrivacyFaq
import com.serona.app.theme.Grey30
import com.serona.app.theme.Heading
import com.serona.app.theme.ParagraphGrey
import com.serona.app.theme.Tertiary
import com.serona.app.theme.figtreeFontFamily

@Composable
fun ExpandablePrivacyItem(
    faq: PrivacyFaq,
    fontSize: TextUnit,
    iconSize: Dp,
    extraPoints: List<String> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faq.question,
                fontSize = fontSize,
                modifier = Modifier.weight(1f),
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal
            )

            Icon(
                imageVector = if (expanded)
                    Icons.Default.KeyboardArrowUp
                else
                    Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Tertiary,
                modifier = Modifier.size(iconSize)
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height((fontSize).value.dp))

            Text(
                text = faq.answer,
                fontSize = fontSize,
                color = Heading,
                lineHeight = fontSize,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.Normal
            )

            if (extraPoints.isNotEmpty()) {
                Spacer(modifier = Modifier.height((fontSize).value.dp))
                Column(verticalArrangement = Arrangement.spacedBy((fontSize).value.dp)) {
                    extraPoints.forEachIndexed { index, point ->
                        NumberedBullet(
                            number = (index + 1).toString(),
                            text = point,
                            fontSize = fontSize
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding((fontSize * 0.4f).value.dp),
            color = Grey30
        )
    }
}

@Composable
fun PrivacyBullet(text: String, fontSize: TextUnit, iconSize: Dp) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Tertiary,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            fontSize = fontSize,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Normal,
            lineHeight = fontSize * 1.4f
        )
    }
}

@Composable
fun PrivacyCard(
    icon: ImageVector,
    text: String,
    modifier: Modifier,
    fontSize: TextUnit,
    iconSize: Dp
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height((fontSize * 6.5f).value.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding((fontSize * 0.2f).value.dp)
        ) {

            // Konten utama
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(icon, contentDescription = null)
                Text(
                    text,
                    fontSize = fontSize,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Normal,
                    lineHeight = fontSize
                )
            }

            // Chevron kanan bawah
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Tertiary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(iconSize * 2)
            )
        }
    }
}

@Composable
fun NumberedBullet(
    number: String,
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number.",
            fontSize = fontSize,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Normal,
            color = ParagraphGrey,
            lineHeight = fontSize * 1.4f
        )
    }
}