package com.example.serona.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.serona.R
import com.example.serona.theme.BodyText
import com.example.serona.theme.Heading
import com.example.serona.theme.MutedLight
import com.example.serona.theme.ParagraphLight
import com.example.serona.theme.Primary
import com.example.serona.theme.PrimaryContainer
import com.example.serona.theme.figtreeFontFamily

@Composable
fun ScannedInfoCard(
    header: String,
    body: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    bgColor: Color = Primary,
    textColor: Color,
    topContent: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(200.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            // 2. TEKS (Column)
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = header,
                    fontSize = fontSize,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    lineHeight = fontSize * 1.2f // Mengatur agar jarak antar baris rapi
                )

                Text(
                    text = body,
                    fontSize = fontSize * 0.8f,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = textColor.copy(alpha = 0.9f),
                    lineHeight = (fontSize * 0.8f) * 1.5f
                )
            }
        }

        // 3. GAMBAR (Diletakkan paling akhir agar di atas / Z-Index)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            topContent()
        }
    }
}

@Composable
fun guideCard(
    Header: String,
    Body: String,
    fontSize: TextUnit,
    iconSize: Dp,
    boxColor: Color,
    icon: ImageVector? = null,
    imageRes: Int? = null,
    imageSize: Float
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .weight(0.11f)
                    .background(
                        color = boxColor,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ){
                if(icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(iconSize * 1.2f)
                    )
                }else if (imageRes != null){
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(imageSize)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.9f)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = Header,
                        fontSize = fontSize,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Heading,
                        lineHeight = (fontSize * 0.9f)
                    )

                    Text(
                        text = Body,
                        fontSize = fontSize * 0.9f,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = ParagraphLight,
                        lineHeight = (fontSize * 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun eventCard(
    label: String,
    fontSize: TextUnit,
    iconTint: Color,
    textColor: Color,
    crcColor: Color,
    bgColor: Color,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .height(105.dp)
            .background(
                color = bgColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = crcColor,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ){
                Icon(
                    imageVector = icon,
                    contentDescription = "Festival",
                    tint = iconTint,
                    modifier = Modifier.fillMaxSize(0.45f)
                )
            }

            Text(
                text = label,
                fontSize = fontSize,
                fontFamily = figtreeFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}