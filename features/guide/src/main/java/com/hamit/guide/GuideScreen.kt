package com.hamit.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.ui.R
import com.hamit.ui.components.clickableEmpty
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

object GuideScreen : Screen {

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
            val navigator = LocalNavigator.currentOrThrow
            TopBar()
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GuideType.entries.forEach {
                    ButtonInstruction(stringResource(it.titleStringRes)) {
                        navigator.push(InstructionFragment(it))
                    }
                }
                Spacer(Modifier.height(10.dp))
                NativeCoordinator.show(Modifier.fillMaxWidth())

            }
        }
    }

}

@Composable
private fun ButtonInstruction(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = AppTypo.APP_TEXT,
            fontSize = 16.sp,
            color = AppColors.WHITE,
            fontFamily = AppFonts.CORE.SEMI_BOLD,
        )
        Spacer(Modifier.width(10.dp))
        Icon(
            modifier = Modifier
                .height(20.dp)
                .rotate(180f),
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "arrow go to guide",
            tint = AppColors.WHITE
        )
    }
}

@Composable
private fun TopBar() {
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
        Text(
            text = stringResource(R.string.instructions),
            style = AppTypo.APP_TEXT,
            fontSize = 18.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.BOLD,
        )
        Box {}
    }
}