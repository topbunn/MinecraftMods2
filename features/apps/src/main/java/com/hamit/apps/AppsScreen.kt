package com.hamit.apps

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.ui.R
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

object AppsScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            2U,
            stringResource(R.string.tabs_apps),
            painterResource(R.drawable.ic_nav_apps)
        )

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BLACK_BG)
                .padding(top = 20.dp)
        ) {
            val context = LocalContext.current
            val viewModel = koinScreenModel<AppsViewModel>()
            val state by viewModel.state.collectAsState()
            LaunchedEffect(state.screenState) {
                if (state.screenState is Error) {
                    Toast.makeText(
                        context,
                        (state.screenState as Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Header()

            PullToRefreshBox(
                modifier = Modifier.weight(1f),
                isRefreshing = false,
                onRefresh = { viewModel.loadApps() }
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    when {
                        state.otherApps.isNotEmpty() -> {
                            state.otherApps.forEach {
                                item {

                                }
                            }
                        }

                        state.screenState is AppsState.AppsStateScreen.Loading -> {
                            item(span = { GridItemSpan(3) }) {
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 20.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = AppColors.WHITE,
                                        strokeWidth = 2.5.dp,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        state.screenState is AppsState.AppsStateScreen.Error -> {
                            item(span = { GridItemSpan(3) }) {

                            }
                        }

                        else -> {
                            item(span = { GridItemSpan(3) }) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.the_list_is_empty),
                                    style = AppTypo.APP_TEXT,
                                    fontSize = 18.sp,
                                    color = AppColors.GRAY,
                                    textAlign = TextAlign.Center,
                                    fontFamily = AppFonts.CORE.BOLD,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Header() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.you_might_like),
                style = AppTypo.APP_TEXT,
                fontSize = 22.sp,
                color = AppColors.GRAY,
                fontFamily = AppFonts.CORE.BOLD,
            )
        }
    }

}