package com.hamit.like

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.like.LikeState.LikeScreenState.Loading
import com.hamit.navigation.Destination
import com.hamit.ui.R
import com.hamit.ui.components.AddonItem
import com.hamit.ui.components.PaginationHandler
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

object LikeScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            0U,
            stringResource(R.string.tabs_favorite),
            painterResource(R.drawable.ic_nav_like)
        )


    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BLACK_BG)
        ) {
            val context = LocalContext.current
            val parentNavigator = LocalNavigator.currentOrThrow.parent
            val viewModel = koinScreenModel<LikeViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.reloadMods()
            }

            LaunchedEffect(state.likeScreenState) {
                if (state.likeScreenState is Error) {
                    Toast.makeText(
                        context,
                        (state.likeScreenState as Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = { viewModel.reloadMods() }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    item { TopBar(state) }
                    itemsIndexed(
                        items = state.addons,
                        key = { _, addon -> addon.id }) { index, addon ->
                        AddonItem(
                            addon = addon,
                            onClickLike = { viewModel.switchLike(addon) },
                            onClickAddon = {
                                viewModel.shouldOpenAddon(addon)
                            }
                        )
                        if ((index != 0 && ((index + 1) % 3 == 0)) || state.addons.size == 1) {
                            Column {
                                Spacer(Modifier.height(10.dp))
                                NativeCoordinator.show(
                                    Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 300.dp)
                                )
                            }
                        }
                    }
                    item {
                        PaginationHandler(
                            isEndOfList = state.addonIsEndList,
                            isLoading = state.likeScreenState is Loading,
                            isError = state.likeScreenState is Error,
                            isEmptyList = state.addons.isEmpty(),
                            loadKey = state.addons.size,
                            onLoadMore = { viewModel.receiveAddons() },
                        )
                    }
                }
            }
            state.shouldOpenAddon?.let {
                val detailScreen = rememberScreen(Destination.AddonScreen(it.id))
                parentNavigator?.push(detailScreen)
                viewModel.shouldOpenAddon(null)
            }
        }
    }

}

@Composable
private fun TopBar(state: LikeState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 14.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.favorite),
            style = AppTypo.APP_TEXT,
            fontSize = 22.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.BOLD,
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "(${state.likeTotalCount ?: "0"})",
            style = AppTypo.APP_TEXT,
            fontSize = 22.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.BOLD,
        )
    }
}