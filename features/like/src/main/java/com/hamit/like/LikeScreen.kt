package com.hamit.like

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.ui.R
import com.hamit.ui.components.addon.AddonList
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors

object LikeScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            1U,
            stringResource(R.string.tabs_favorite),
            painterResource(R.drawable.ic_nav_like)
        )

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<LikeViewModel>()
        val state by viewModel.state.collectAsState()
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize()
                .statusBarsPadding(),
            isRefreshing = false,
            onRefresh = { viewModel.refreshAddons() }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Title(state.likeTotalCount)
                AddonList(
                    modifier = Modifier.fillMaxSize(),
                    addons = state.addons,
                    state = state.listState,
                    status = state.addonStatus,
                    isEndOfList = state.addonIsEndList,
                    paddingValues = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    onClick = viewModel::openAddon,
                    onPreload = { viewModel.receiveAddons() },
                )
            }
        }
    }

    @Composable
    private fun Title(likeCount: Int?){
        val colors = LocalAppColors.current
        Text(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 10.dp),
            text = "${stringResource(R.string.favorite)} ${likeCount ?: ""}",
            style = AppTypo.H2,
            color = colors.title
        )
    }

}