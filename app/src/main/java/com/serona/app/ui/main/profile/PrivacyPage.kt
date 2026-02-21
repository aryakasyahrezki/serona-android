package com.serona.app.ui.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serona.app.theme.BgGrad
import com.serona.app.ui.component.ExpandablePrivacyItem
import com.serona.app.ui.component.PrivacyBullet
import com.serona.app.ui.component.PrivacyCard
import com.serona.app.theme.ParagraphGrey
import com.serona.app.theme.Tertiary
import com.serona.app.theme.White
import com.serona.app.theme.figtreeFontFamily
import com.serona.app.ui.component.BackButton
import com.serona.app.utils.ResponsiveScale

@Composable
fun PrivacyPage(
    privacyViewModel: PrivacyViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val faqList by privacyViewModel.faqList.collectAsState()
    val privacyPoints = stringArrayResource(id = com.serona.app.R.array.privacy_points)

    ResponsiveScale(maxFontScale = 1f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
        ) {
            val configuration = LocalConfiguration.current
            val maxWidth = configuration.screenWidthDp.dp
            val maxHeight = configuration.screenHeightDp.dp

            val fontSize = (maxWidth * 0.06f).value.sp
            val iconSize = (maxHeight * 0.03f)
            val upperBoxHeight = maxHeight * 0.44f
            val horiPadding = maxWidth * 0.05f
            val vertiPadding = maxHeight * 0.055f
            val space = maxHeight * 0.03f
            val buttonSize = maxWidth * 0.07f

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(upperBoxHeight)
                    .background(BgGrad)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horiPadding, vertical = vertiPadding)
            ) {
                Spacer(modifier = Modifier.height(space * 0.15f))

                BackButton(
                    onBackClick = { onBackClick() },
                    buttonSize = buttonSize,
                    fontSize = fontSize
                )

                Spacer(modifier = Modifier.height(space))

                Column() {
                    Text(
                        text = "Serona Privacy Explanation",
                        fontSize = fontSize,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Tertiary
                    )

                    Spacer(modifier = Modifier.height(space * 0.1f))

                    Text(
                        text = "No worries - your privacy is always protected with Serona.\nHere are the key things you should know:",
                        fontSize = fontSize * 0.53f,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = ParagraphGrey
                    )

                    Spacer(modifier = Modifier.height(space * 0.3f))

                    PrivacyBullet(
                        "We do not share or sell your data to anyone",
                        fontSize = fontSize * 0.5f,
                        iconSize = iconSize * 0.7f
                    )
                    PrivacyBullet(
                        "Your data is protected with encryption",
                        fontSize = fontSize * 0.5f,
                        iconSize = iconSize * 0.7f
                    )
                    PrivacyBullet(
                        "You're free to delete your data at any time",
                        fontSize = fontSize * 0.5f,
                        iconSize = iconSize * 0.7f
                    )
                    PrivacyBullet(
                        "We only collect information that is strictly necessary to operate Serona's features",
                        fontSize = fontSize * 0.5f,
                        iconSize = iconSize * 0.7f
                    )

                    Spacer(modifier = Modifier.height(space * 0.8f))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PrivacyCard(
                            icon = Icons.Outlined.Lock,
                            text = "You control your data",
                            modifier = Modifier.weight(0.3f),
                            fontSize = fontSize * 0.5f,
                            iconSize = iconSize * 0.3f
                        )

                        Spacer(modifier = Modifier.width(space * 0.3f))

                        PrivacyCard(
                            icon = Icons.Outlined.Security,
                            text = "We protect your privacy",
                            modifier = Modifier.weight(0.3f),
                            fontSize = fontSize * 0.5f,
                            iconSize = iconSize * 0.3f
                        )

                        Spacer(modifier = Modifier.width(space * 0.3f))

                        PrivacyCard(
                            icon = Icons.Outlined.PersonOutline,
                            text = "You are the one who decides",
                            modifier = Modifier.weight(0.3f),
                            fontSize = fontSize * 0.5f,
                            iconSize = iconSize * 0.3f
                        )
                    }

                    Spacer(modifier = Modifier.height(space * 0.5f))

                    Text(
                        text = "Response to your statement",
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(space * 0.5f))
                }

                LazyColumn() {
                    itemsIndexed(faqList) { index, faq ->
                        ExpandablePrivacyItem(
                            faq = faq,
                            fontSize = fontSize * 0.6f,
                            iconSize = iconSize,
                            extraPoints = if (index == 0) privacyPoints.toList() else emptyList()
                        )
                    }
                }
            }
        }
    }
}