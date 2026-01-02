package com.hamit.guide

import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.android.ads.natives.NativeCoordinator
import kotlinx.android.parcel.Parcelize
import com.hamit.ui.R
import com.hamit.ui.components.clickableEmpty
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

@Parcelize
class InstructionFragment(private val guideType: GuideType) : Screen, Parcelable {

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.GRAY_BG)
                .navigationBarsPadding()
                .statusBarsPadding()
                .background(AppColors.BLACK_BG)
        ) {
            TopBar(guideType)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                val instructions = when (guideType) {
                    GuideType.ADDON -> GuideEntity.getAddonGuide()
                    GuideType.WORLD -> GuideEntity.getWorldGuide()
                }
                instructions.forEachIndexed { index, item ->
                    GuideItem(
                        title = "${index + 1} " + stringResource(item.titleResourceId),
                        image = painterResource(item.previewResourceId)
                    )
                }
                NativeCoordinator.show(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun GuideItem(title: String, image: Painter) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = AppTypo.APP_TEXT,
            fontSize = 18.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.SEMI_BOLD,
        )
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = image,
            contentDescription = title,
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun TopBar(type: GuideType) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.GRAY_BG)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navigator = LocalNavigator.currentOrThrow
        Icon(
            modifier = Modifier
                .height(20.dp)
                .clickableEmpty { navigator.pop() },
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "button back",
            tint = MaterialTheme.colorScheme.primary
        )
        val titleRes = when (type) {
            GuideType.ADDON -> R.string.installing_addons_and_textures
            GuideType.WORLD -> R.string.installation_of_worlds
        }
        Text(
            text = stringResource(titleRes),
            style = AppTypo.APP_TEXT,
            fontSize = 18.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.BOLD,
        )
        Box {}
    }
}