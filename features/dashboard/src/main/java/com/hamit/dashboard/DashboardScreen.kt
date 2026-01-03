package com.hamit.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.hamit.navigation.Destination
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow

object DashboardScreen : Screen {

    @Composable
    override fun Content() {
        val homeScreen = rememberScreen(Destination.HomeScreen) as Tab
        val colors = LocalAppColors.current
        TabNavigator(tab = homeScreen) {
            Scaffold(
                content = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(colors.background)
                            .padding(it)
                    ) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .appDropShadow(shape = RectangleShape, offset = DpOffset.Zero, radius = 4.dp)
                            .background(colors.card)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val likeScreen = rememberScreen(Destination.LikeScreen) as Tab
                        val suggestScreen = rememberScreen(Destination.SuggestScreen) as Tab
                        BottomNavBar(homeScreen)
                        BottomNavBar(likeScreen)
                        BottomNavBar(suggestScreen)
                    }
                }
            )
        }
    }

    @Composable
    private fun RowScope.BottomNavBar(tab: Tab) {
        val colors = LocalAppColors.current
        val tabNavigator = LocalTabNavigator.current
        val selected = tabNavigator.current == tab
        val interaction = remember { MutableInteractionSource() }
        val backgroundColor = if (selected) colors.primaryContainer else Color.Transparent
        Box(
            modifier = Modifier.weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .clickable(indication = ripple(color = colors.primaryContainer), interactionSource = interaction) {
                    tabNavigator.current = tab
                }.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ){
            tab.options.icon?.let {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = it,
                    contentDescription = tab.options.title,
                    tint = colors.primary
                )
            }
        }
    }

}