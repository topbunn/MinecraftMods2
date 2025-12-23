package com.hamit.main

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
import com.hamit.main.MainState.MainScreenState.Loading
import com.hamit.navigation.Destination
import com.hamit.ui.R
import com.hamit.ui.components.CustomDropDown
import com.hamit.ui.components.CustomTextField
import com.hamit.ui.components.ModsList
import com.hamit.ui.components.CustomTabRow
import com.hamit.ui.components.clickableEmpty
import com.hamit.ui.theme.AppColors

object MainScreen : Tab, Screen {

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
            val viewModel = koinScreenModel<MainViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.handleChangeState()
            }

            LaunchedEffect(state.mainScreenState) {
                if (state.mainScreenState is Error) {
                    Toast.makeText(
                        context,
                        (state.mainScreenState as Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            TopBar(
                value = state.search,
                onValueChange = { viewModel.changeSearch(it) },
                onClickFavorite = {
                    navigator.push(favoriteScreen)
                }
            )
            Spacer(Modifier.height(20.dp))
            SortBar(
                modTypeUis = state.modTypeUis,
                modSorts = state.modSorts,
                modSortSelectedIndex = state.modSortSelectedIndex,
                selectedModTypeUi = state.selectedModTypeUi,
                changeModSort = { viewModel.changeModSort(it) },
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
                ModsList(
                    modifier = Modifier.fillMaxSize(),
                    mods = state.mods,
                    state = state.modListState,
                    isError = state.mainScreenState is Error,
                    isLoading = state.mainScreenState is Loading,
                    isEndList = state.isEndList,
                    onLoad = { viewModel.loadMods() },
                    onClickFavorite = { viewModel.changeFavorite(it) },
                    onClickMod = { viewModel.changeOpenMod(it) }
                )
            }
            state.openMod?.let {
                val detailScreen = rememberScreen(Destination.AddonScreen(it.id))
                parentNavigator?.push(detailScreen)
                viewModel.changeOpenMod(null)
            }
        }
    }

}


@Composable
private fun SortBar(
    modTypeUis: List<ModTypeUi>,
    modSorts: List<ModSortTypeUi>,
    modSortSelectedIndex: Int,
    selectedModTypeUi: ModTypeUi,
    changeModSort: (Int) -> Unit,
    changeSortType: (ModTypeUi) -> Unit,
) {
    val mapSortWithTitle = modTypeUis.map {
        it to stringResource(it.titleRes)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CustomTabRow(
            modifier = Modifier.weight(1f),
            content = modSorts.map { stringResource(it.stringRes) },
            selected = modSortSelectedIndex
        ) {
            changeModSort(it)
        }
        CustomDropDown(
            value = stringResource(selectedModTypeUi.titleRes),
            items = modTypeUis.map { stringResource(it.titleRes) }
        ) { title ->
            changeSortType(mapSortWithTitle.first { it.second == title }.first)
        }
    }
}


@Composable
private fun TopBar(
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
            value = value,
            placeholder = stringResource(R.string.search),
            onValueChange = onValueChange,
            iconStart = {
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
