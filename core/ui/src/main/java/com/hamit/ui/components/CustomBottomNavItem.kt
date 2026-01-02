package com.hamit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

@Composable
fun RowScope.CustomBottomNavItem(
    itemTab: Tab,
) {
    val navigator = LocalTabNavigator.current
    val isSelected = navigator.current == itemTab
    val touchSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(
                indication = null,
                interactionSource = touchSource
            ) {
                navigator.current = itemTab
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tintColor =
            if (isSelected) MaterialTheme.colorScheme.primary
            else AppColors.WHITE.copy(0.5f)

        itemTab.options.icon?.let { iconPainter ->
            Icon(
                modifier = Modifier.size(24.dp),
                painter = iconPainter,
                contentDescription = itemTab.options.title,
                tint = tintColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = itemTab.options.title,
            style = AppTypo.APP_TEXT,
            fontSize = 14.sp,
            color = tintColor,
            fontFamily = AppFonts.CORE.SEMI_BOLD,
        )
    }
}
