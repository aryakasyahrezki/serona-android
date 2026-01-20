package com.example.serona.ui.component

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serona.R
import com.example.serona.theme.Grey40
import com.example.serona.theme.Primary
import com.example.serona.theme.White

@Composable
fun NavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onCenterClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        val barHeight = screenHeight * 0.1f
        val fabSize = barHeight * 1.2f
        val maxWidth = screenWidth

        val iconSize = barHeight * 0.3f
        val textSize = (barHeight * 0.17f).value.sp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight + fabSize / 2)
        ){
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .align(Alignment.BottomCenter),
                color = White,
                shape = NavBarShape(),
                shadowElevation = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = maxWidth * 0.07f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavBarItem(
                        "Home",
                        icon = Icons.Outlined.Home,
                        iconSize = iconSize,
                        textSize = textSize,
                        selectedIndex == 0
                    ){
                        onItemSelected(0)
                    }

                    NavBarItem(
                        "Tutorial",
                        icon = Icons.Outlined.Book,
                        iconSize = iconSize,
                        textSize = textSize,
                        selectedIndex == 1
                    ){
                        onItemSelected(1)
                    }

                    Spacer(modifier = Modifier.width(iconSize))

                    NavBarItem(
                        "Favorite",
                        icon = Icons.Outlined.FavoriteBorder,
                        iconSize = iconSize,
                        textSize = textSize,
                        selectedIndex == 2
                    ){
                        onItemSelected(2)
                    }

                    NavBarItem(
                        "Profile",
                        icon = Icons.Outlined.PersonOutline,
                        iconSize = iconSize,
                        textSize = textSize,
                        selectedIndex == 3
                    ){
                        onItemSelected(3)
                    }
                }
            }

            FloatingActionButton(
                onClick = onCenterClick,
                containerColor = Primary,
                modifier = Modifier
                    .size(65.dp)
                    .align(Alignment.TopCenter),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ar_on_you),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(iconSize *1.25f)
                )
            }
        }
    }
}

@Composable
fun NavBarItem(
    label:String,
    icon: ImageVector,
    iconSize: Dp,
    textSize: TextUnit,
    selected:Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) Primary else Grey40

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable{onClick()}
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = label,
            fontSize = textSize,
            color = color
        )
    }
}

@Preview
@Composable
private fun navbarPrev() {
    var selectedIndex = 2

    NavBar(
        selectedIndex = selectedIndex,
        onItemSelected = { selectedIndex = it },
        onCenterClick = { /* scan / camera */ }
    )
}
