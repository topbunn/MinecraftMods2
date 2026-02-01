package com.l13devstudio.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.l13devstudio.ui.components.AppTextField
import com.l13devstudio.ui.components.addon.AddonList
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.utils.ObserveAsEvents
import com.l13devstudio.ui.utils.appDropShadow


object HomeScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            0U,
            stringResource(R.string.tabs_main),
            painterResource(R.drawable.ic_nav_main)
        )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<HomeViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow.parent
        val colors = LocalAppColors.current
        LaunchedEffect(Unit) { viewModel.handleChangeState() }
        ObserveAsEvents(viewModel.events) {
            when(it){
                is HomeEvent.OpenMod -> {
                    val screen = ScreenRegistry.get(Destination.AddonScreen(it.id))
                    navigator?.push(screen)
                }
            }
        }
        Column {
            Header(
                query = state.query,
                onChangeQuery = viewModel::changeQuery,
                onClickFilter = { viewModel.changeFilterDialog(true) },
            )
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isRefreshing = false,
                onRefresh = { viewModel.refreshAddons() }
            ) {
                AddonList(
                    addons = state.addons,
                    modifier = Modifier.fillMaxSize(),
                    state = state.listState,
                    status = state.addonStatus,
                    isEndOfList = state.isEndOfList,
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
                    onPreload = {
                        viewModel.loadAddons()
                    },
                )
            }
        }
        FilterDialog(state, viewModel)
    }

    @Composable
    private fun FilterDialog(state: HomeState, viewModel: HomeViewModel) {
        if (state.filterIsOpen) {
            FilterDialog(
                sortTypeItems = state.sortTypes,
                addonTypeItems = state.addonTypes,
                selectedSortTypeIndex = state.selectedSortTypeIndex,
                selectedAddonTypeIndex = state.selectedAddonTypeIndex,
                onSelectSortType = { viewModel.changeFilterValue(it, HomeState.FilterType.SORTS) },
                onSelectAddonType = {
                    viewModel.changeFilterValue(
                        it,
                        HomeState.FilterType.ADDON_TYPES
                    )
                }
            ) {
                viewModel.changeFilterDialog(false)
            }
        }
    }

    @Composable
    private fun Header(
        query: String,
        onChangeQuery: (String) -> Unit,
        onClickFilter: () -> Unit
    ) {
        val colors = LocalAppColors.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .appDropShadow(shape = RectangleShape, alpha = 0.1f)
                .background(colors.card)
                .statusBarsPadding()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                text = query,
                fontSize = 16.sp,
                paddingValues = PaddingValues(start = 17.dp, top = 17.dp, bottom = 17.dp, end = 12.dp),
                hint = stringResource(R.string.search),
                leadingIconRes = R.drawable.ic_search,
                onTextChange = onChangeQuery
            )
            FilterButton(
                onClick = onClickFilter
            )
        }
    }

    @Composable
    private fun FilterButton(onClick: () -> Unit) {
        val colors = LocalAppColors.current
        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, colors.border, RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.primaryContainer),
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_filters),
                contentDescription = null,
                tint = colors.primary
            )
        }
    }

}