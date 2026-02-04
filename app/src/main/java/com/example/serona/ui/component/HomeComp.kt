package com.example.serona.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.serona.theme.Heading
import com.example.serona.theme.ParagraphLight
import com.example.serona.theme.Primary
import com.example.serona.theme.figtreeFontFamily

@Composable
fun ScannedInfoCard(
    header: String,
    body: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    bgColor: Color = Primary,
    textColor: Color,
    screenHeight: Dp,
    topContent: @Composable () -> Unit
) {

    val space = (screenHeight * 0.04f)

    Box(
        modifier = modifier
            .height(screenHeight * 0.25f),
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
                .padding(space * 0.35f)
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
    imageSize: Float,
    screenHeight: Dp,
    screenWidth: Dp
) {

    val space = (screenHeight * 0.04f)

    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.02f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(screenHeight * 0.05f)
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
    icon: ImageVector,
    screenHeight: Dp,
    screenWidth: Dp,
    onClick : () -> Unit
) {
    val space = (screenHeight * 0.04f)

    Box(
        modifier = Modifier
            .width(screenWidth * 0.23f)
            .height(screenHeight * 0.13f)
            .background(
                color = bgColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
            .clickable(onClick = {onClick()}),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = space * 0.3f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = crcColor,
                        shape = CircleShape
                    )
                    .padding(space * 0.1f)
            ){
                Icon(
                    imageVector = icon,
                    contentDescription = null,
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

