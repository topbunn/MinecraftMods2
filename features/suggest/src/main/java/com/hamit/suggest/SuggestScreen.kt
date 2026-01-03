package com.hamit.suggest

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.ui.R

object SuggestScreen: Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            2U,
            stringResource(R.string.tabs_suggest),
            painterResource(R.drawable.ic_nav_suggest)
        )

    @Composable
    override fun Content() {

    }
}