package com.hamit.addon

import android.Manifest
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hamit.addon.dontWorkAddon.DontWorkAddonDialog
import com.hamit.addon.installAddon.InstallAddonDialog
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.android.utills.getModNameFromUrl
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.navigation.Destination
import com.hamit.ui.R
import com.hamit.ui.components.CustomButton
import com.hamit.ui.components.clickableEmpty
import com.hamit.ui.components.clickableRipple
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.utils.permissions
import kotlinx.parcelize.Parcelize
import org.koin.core.parameter.parametersOf

@Parcelize
data class AddonScreen(private val addonId: Int) : Screen, Parcelable {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val activity = LocalActivity.currentOrThrow

        val viewModel = koinScreenModel<AddonViewModel> { parametersOf(addonId) }
        val state by viewModel.state.collectAsState()
        val loadModState = state.loadAddonState

        var interAdIsShown by rememberSaveable {
            mutableStateOf(false)
        }

        if (!interAdIsShown) {
            InterstitialCoordinator.show(activity)
            interAdIsShown = true
        }

        permissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        LaunchedEffect(loadModState) {
            if (loadModState is AddonState.LoadAddonState.Error) {
                Toast.makeText(
                    activity.application,
                    "Loading error. Check internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.GRAY_BG)
                .navigationBarsPadding()
                .statusBarsPadding()
                .background(AppColors.BLACK_BG)
        ) {
            TopBar(
                mod = state.addon,
                onClickChangeFavorite = { viewModel.switchLikeStatus() }
            )
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isRefreshing = false,
                onRefresh = { viewModel.loadAddon() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {

                    state.addon?.let {
                        ButtonGuide(navigator)
                        Spacer(Modifier.height(10.dp))
                        Image(it)
                        Spacer(Modifier.height(20.dp))
                        TextContent(
                            mod = state.addon,
                            descriptionTextExpand = state.textIsExpand,
                            descriptionImageExpand = state.imageIsExpand,
                            onClickSwitchDescriptionImage = viewModel::switchImageExpand,
                            onClickSwitchDescriptionText = viewModel::switchTextExpand
                        )
                        Spacer(Modifier.height(20.dp))
                        Versions(mod = state.addon)
                        Spacer(Modifier.height(20.dp))
                        NativeCoordinator.show(Modifier.fillMaxWidth())
                        Spacer(Modifier.height(20.dp))
                        FileButtons(
                            mod = state.addon,
                            onClickMod = { viewModel.changePathFile(it) },
                            onClickAddonNotWork = { viewModel.shouldOpenIssue(true) },
                        )
                    }
                }

                Box(Modifier.fillMaxWidth(), Alignment.Center) {
                    when (loadModState) {
                        is AddonState.LoadAddonState.Error -> {
                            CustomButton(
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(R.string.retry)
                            ) { viewModel.loadAddon() }
                        }

                        AddonState.LoadAddonState.Loading -> {
                            Box(Modifier.padding(vertical = 20.dp)) {
                                CircularProgressIndicator(
                                    color = AppColors.WHITE,
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
        state.addon?.let { mod ->
            state.pathFile?.let {
                InstallAddonDialog(
                    it.getModNameFromUrl(mod.category.toExtension()),
                    viewModel
                ) {
                    viewModel.changePathFile(null)
                }
            }
            if (state.shouldOpenIssue) {
                DontWorkAddonDialog() { viewModel.shouldOpenIssue(false) }
            }
        }
    }

}


@Composable
private fun FileButtons(
    mod: AddonEntity?,
    onClickMod: (path: String) -> Unit,
    onClickAddonNotWork: () -> Unit,
) {
    mod?.let { mod ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            mod.files.forEach {
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    label = it.getModNameFromUrl(mod.category.toExtension()),
                    foregroundColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(0.4f),
                ) {
                    onClickMod(it)
                }
            }
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                label = stringResource(R.string.addon_don_t_work),
                foregroundColor = AppColors.WHITE,
                backgroundColor = Color(0xffE03131),
            ) {
                onClickAddonNotWork()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Versions(
    mod: AddonEntity?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.supported_versions),
            style = AppTypo.APP_TEXT,
            fontSize = 18.sp,
            color = AppColors.WHITE,
            fontFamily = AppFonts.CORE.SEMI_BOLD,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            mod?.versions?.forEach { version ->
                VersionItem(
                    value = version,
                )
            }
        }
    }
}

@Composable
private fun VersionItem(value: String, actualVersion: Boolean = false) {
    Text(
        modifier = Modifier
            .background(
                if (actualVersion) MaterialTheme.colorScheme.primary else AppColors.WHITE,
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        text = value,
        style = AppTypo.APP_TEXT,
        fontSize = 15.sp,
        color = AppColors.BLACK_BG,
        fontFamily = AppFonts.CORE.SEMI_BOLD,
    )
}

@Composable
private fun TextContent(
    mod: AddonEntity?,
    descriptionTextExpand: Boolean,
    descriptionImageExpand: Boolean,
    onClickSwitchDescriptionImage: () -> Unit,
    onClickSwitchDescriptionText: () -> Unit,
) {
    mod?.let { mod ->
        Text(
            text = mod.title,
            style = AppTypo.APP_TEXT,
            fontSize = 24.sp,
            color = AppColors.WHITE,
            fontFamily = AppFonts.CORE.BOLD,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = if (descriptionTextExpand) mod.description else mod.description.take(300) + "...",
            style = AppTypo.APP_TEXT,
            fontSize = 14.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.MEDIUM,
        )
        Spacer(Modifier.height(10.dp))
        if (mod.description.length > 300) {
            Box(Modifier.fillMaxWidth(), Alignment.CenterEnd) {
                Row(
                    modifier = Modifier
                        .clickableRipple() { onClickSwitchDescriptionText() }
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(if (descriptionTextExpand) R.string.collapse else R.string.expand),
                        style = AppTypo.APP_TEXT,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = AppFonts.CORE.BOLD,
                    )
                    Icon(
                        modifier = Modifier.rotate(if (descriptionTextExpand) 180f else 0f),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Choice type",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val context = LocalContext.current
            val countTake = if (descriptionImageExpand) Int.MAX_VALUE else 3
            mod.descriptionImages.take(countTake).forEach {
                val request = remember(mod.image) {
                    ImageRequest.Builder(context)
                        .data(it)
                        .crossfade(false)
                        .memoryCacheKey(mod.image)
                        .diskCacheKey(mod.image)
                        .build()
                }
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    model = request,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        if (mod.descriptionImages.count() > 5) {
            Box(Modifier.fillMaxWidth(), Alignment.CenterEnd) {
                Row(
                    modifier = Modifier
                        .clickableRipple() { onClickSwitchDescriptionImage() }
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(if (descriptionImageExpand) R.string.collapse else R.string.expand),
                        style = AppTypo.APP_TEXT,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = AppFonts.CORE.BOLD,
                    )
                    Icon(
                        modifier = Modifier.rotate(if (descriptionImageExpand) 180f else 0f),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Choice type",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun Image(mod: AddonEntity) {
    val context = LocalContext.current
    val request = remember(mod.image) {
        ImageRequest.Builder(context)
            .data(mod.image)
            .crossfade(false)
            .memoryCacheKey(mod.image)
            .diskCacheKey(mod.image)
            .build()
    }
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        model = request,
        contentDescription = mod.title,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun ButtonGuide(navigator: Navigator) {
    val instructionScreen = rememberScreen(Destination.GuideScreen)
    CustomButton(
        label = stringResource(R.string.instructions),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        navigator.push(instructionScreen)
    }
}

@Composable
private fun TopBar(
    mod: AddonEntity?,
    onClickChangeFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.GRAY_BG)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navigator = LocalNavigator.currentOrThrow
        Icon(
            modifier = Modifier
                .height(20.dp)
                .clickableEmpty { navigator.pop() },
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "button back",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.installation),
            style = AppTypo.APP_TEXT,
            fontSize = 18.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.BOLD,
        )

        Image(
            modifier = Modifier
                .size(24.dp)
                .clickableEmpty { onClickChangeFavorite() },
            painter = painterResource(
                if (mod?.isLike
                        ?: false
                ) R.drawable.ic_mine_like_filled else R.drawable.ic_mine_like_stroke
            ),
            contentDescription = "favorite mods",
        )
    }
}