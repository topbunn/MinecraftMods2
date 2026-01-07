package com.hamit.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.domain.entity.addon.AddonType
import com.hamit.ui.R
import com.hamit.ui.components.AddonItem
import com.hamit.ui.components.AppTextField
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow


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
        Column {
            Header{ viewModel.changeFilterDialog(true) }
            AddonList()
        }
        FilterDialog(state, viewModel)
    }

    @Composable
    private fun ColumnScope.AddonList() {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 20.dp)
        ) {
            val list = mutableListOf<AddonEntity>().apply {
                repeat(10){
                    val addon = AddonEntity(
                        id = it,
                        name = "Anime Clouds - Custom Sky $it",
                        desc = "This texture pack transforms the default Minecraft sky into a stunning scene with soft beautiful anime-style sky full of soft, fluffy clouds! It looks like your favorite anime shows and makes your world feel more magical and fun.\u2028Perfect for building, exploring, or just relaxing.",
                        preview = "https://media.forgecdn.net/attachments/1271/256/img_20250725_161123-jpg.jpg",
                        type = AddonType.ADDON,
                        isLike = false,
                        stars = 5.0,
                        commentCounts = 5,
                        images = emptyList(),
                        files = emptyList(),
                        versions = listOf("1.18", "1.19", "1.20", "1.21", "1.21.1", "1.21.2")
                    )
                    add(addon)
                }
            }
            items(items = list, key = {it.id}){
                AddonItem(it)
            }
        }
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
            var text by remember { mutableStateOf("") }
            AppTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                text = text,
                fontSize = 18.sp,
                paddingValues = PaddingValues(start = 17.dp, top = 17.dp, bottom = 17.dp, end = 12.dp),
                hint = stringResource(R.string.search),
                leadingIconRes = R.drawable.ic_search
            ) {
                text = it
            }
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