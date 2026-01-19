package com.hamit.guide

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.guide.GuideType.ADDON
import com.hamit.guide.GuideType.WORLD
import com.hamit.ui.R
import com.hamit.ui.components.SelectableRow
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow

object GuideScreen : Screen {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

        val guideTypes = GuideType.entries
        var selectedGuideTypeIndex by rememberSaveable { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Header { navigator.pop() }
            Spacer(Modifier.height(10.dp))
            SelectableRow(
                selectedIndex = selectedGuideTypeIndex,
                items = guideTypes.map { it.titleStringRes },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                selectedGuideTypeIndex = it
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
            ) {
                val items = when(guideTypes[selectedGuideTypeIndex]){
                    ADDON -> GuideEntity.getAddonGuide(context)
                    WORLD -> GuideEntity.getMapGuide(context)
                }
                itemsIndexed(items = items){ index, guide ->
                    GuideItem(index, guide)
                }
            }
        }
    }

    @Composable
    private fun GuideItem(index: Int, guide: GuideEntity) {
        val colors = LocalAppColors.current
        Column{
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = "${index + 1}. ${guide.text}",
                color = colors.text,
                fontFamily = AppFonts.CORE,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(8.dp))
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                painter = painterResource(guide.imageResId),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
    }


    @Composable
    private fun Header(
        onClickBack: () -> Unit
    ) {
        val colors = LocalAppColors.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .appDropShadow(CircleShape)
                    .clip(CircleShape)
                    .background(colors.card)
                    .clickable(onClick = onClickBack)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = colors.primary
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.instructions),
                style = AppTypo.H2,
                color = colors.title
            )
        }
    }

}