package com.l13devstudio.like

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.l13devstudio.android.ads.natives.NativeCoordinator
import com.l13devstudio.navigation.Destination
import com.l13devstudio.ui.R
import com.l13devstudio.ui.components.addon.AddonList
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.utils.ObserveAsEvents
import com.l13devstudio.ui.utils.appDropShadow

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
        val navigator = LocalNavigator.currentOrThrow.parent
        val colors = LocalAppColors.current
        ObserveAsEvents(viewModel.events) {
            when(it){
                is LikeEvent.OpenMod -> {
                    val addonScreen = ScreenRegistry.get(Destination.AddonScreen(it.id))
                    navigator?.push(addonScreen)
                }
            }
        }
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
                    adContent = {
                        NativeCoordinator.show(
                            modifier = Modifier.fillMaxWidth()
                                .appDropShadow(RoundedCornerShape(24.dp))
                                .clip(RoundedCornerShape(24.dp))
                                .background(color = colors.card),
                            type = NativeCoordinator.ViewAdType.Native,
                        )
                    },
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
            text = "${stringResource(R.string.favorite)} (${likeCount ?: ""})",
            style = AppTypo.H2,
            color = colors.title
        )
    }

}