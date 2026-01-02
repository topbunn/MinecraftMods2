package com.hamit.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.home.HomeState.HomeScreenState.Loading
import com.hamit.navigation.Destination
import com.hamit.ui.R
import com.hamit.ui.components.AddonsList
import com.hamit.ui.components.CustomDropDown
import com.hamit.ui.components.CustomTextField
import com.hamit.ui.components.CustomTabRow
import com.hamit.ui.components.clickableEmpty
import com.hamit.ui.theme.AppColors

object HomeScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            0U,
            stringResource(R.string.tabs_main),
            painterResource(R.drawable.ic_nav_main)
        )

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BLACK_BG)
                .padding(top = 24.dp, start = 20.dp, end = 20.dp)
        ) {
            val navigator = LocalNavigator.currentOrThrow
            val parentNavigator = LocalNavigator.currentOrThrow.parent
            val favoriteScreen = rememberScreen(Destination.LikeScreen)

            val context = LocalContext.current
            val viewModel = koinScreenModel<HomeViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.handlingStateChanged()
            }

            LaunchedEffect(state.homeScreenState) {
                if (state.homeScreenState is Error) {
                    Toast.makeText(
                        context,
                        (state.homeScreenState as Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Header(
                value = state.query,
                onValueChange = { viewModel.changeQuery(it) },
                onClickFavorite = {
                    navigator.push(favoriteScreen)
                }
            )
            Spacer(Modifier.height(20.dp))
            SortedSettings(
                addonTypeUis = state.addonTypeUis,
                modSorts = state.homeSorts,
                modSortSelectedIndex = state.addonSortSelectedIndex,
                selectedAddonTypeUi = state.selectedAddonTypeUi,
                changeModSort = { viewModel.changeAddonSort(it) },
                changeSortType = { viewModel.changeSortType(it) },
            )
            Spacer(
                Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xff464646))
            )
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isRefreshing = false,
                onRefresh = { viewModel.refreshMods() }
            ) {
                AddonsList(
                    modifier = Modifier.fillMaxSize(),
                    addons = state.addons,
                    listState = state.addonListState,
                    isError = state.homeScreenState is Error,
                    isLoading = state.homeScreenState is Loading,
                    isEndList = state.isAddonListEnd,
                    onLoadMore = { viewModel.loadMods() },
                    onClickFavorite = { viewModel.switchLike(it) },
                    onClickAddon = { viewModel.shouldOpenAddon(it) }
                )
            }
            state.shouldAddonOpen?.let {
                val detailScreen = rememberScreen(Destination.AddonScreen(it.id))
                parentNavigator?.push(detailScreen)
                viewModel.shouldOpenAddon(null)
            }
        }
    }

}


@Composable
private fun SortedSettings(
    addonTypeUis: List<AddonTypeUi>,
    modSorts: List<AddonSortTypeUi>,
    modSortSelectedIndex: Int,
    selectedAddonTypeUi: AddonTypeUi,
    changeModSort: (Int) -> Unit,
    changeSortType: (AddonTypeUi) -> Unit,
) {
    val mapSortWithTitle = addonTypeUis.map {
        it to stringResource(it.titleStringRes)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CustomTabRow(
            modifier = Modifier.weight(1f),
            content = modSorts.map { stringResource(it.stringResourceId) },
            selected = modSortSelectedIndex
        ) {
            changeModSort(it)
        }
        CustomDropDown(
            selectedValue = stringResource(selectedAddonTypeUi.titleStringRes),
            options = addonTypeUis.map { stringResource(it.titleStringRes) }
        ) { title ->
            changeSortType(mapSortWithTitle.first { it.second == title }.first)
        }
    }
}


@Composable
private fun Header(
    value: String,
    onValueChange: (String) -> Unit,
    onClickFavorite: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomTextField(
            modifier = Modifier.weight(1f),
            textValue = value,
            hint = stringResource(R.string.search),
            onTextChanged = onValueChange,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "search",
                    tint = AppColors.GRAY
                )
            }
        )
        Image(
            modifier = Modifier
                .size(28.dp)
                .clickableEmpty { onClickFavorite() },
            painter = painterResource(R.drawable.ic_mine_like_filled),
            contentDescription = "favorite mods",
        )
    }
}
