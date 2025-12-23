package ru.topbun.detail_mod

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
import kotlinx.parcelize.Parcelize
import org.koin.core.parameter.parametersOf
import ru.topbun.android.ad.inter.InterAdInitializer
import ru.topbun.android.ad.natives.NativeApplovinView
import ru.topbun.android.utils.getModNameFromUrl
import ru.topbun.detail_mod.dontWorkAddon.DontWorkAddonDialog
import ru.topbun.detail_mod.setupMod.SetupModDialog
import ru.topbun.domain.entity.mod.ModEntity
import ru.topbun.navigation.SharedScreen
import ru.topbun.ui.R
import ru.topbun.ui.components.CustomButton
import ru.topbun.ui.components.IconWithButton
import ru.topbun.ui.components.clickableEmpty
import ru.topbun.ui.components.clickableRipple
import ru.topbun.ui.theme.AppColors
import ru.topbun.ui.theme.AppFonts
import ru.topbun.ui.theme.AppTypo
import ru.topbun.ui.utils.permissions

@Parcelize
data class DetailModScreen(private val modId: Int) : Screen, Parcelable {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val activity = LocalActivity.currentOrThrow

        val viewModel = koinScreenModel<DetailModViewModel> { parametersOf(modId) }
        val state by viewModel.state.collectAsState()
        val loadModState = state.loadModState

        var interAdIsShown by rememberSaveable {
            mutableStateOf(false)
        }

        if (!interAdIsShown) {
            InterAdInitializer.show(activity)
            interAdIsShown = true
        }

        permissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        LaunchedEffect(loadModState) {
            if (loadModState is DetailModState.LoadModState.Error) {
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
            Header(
                mod = state.mod,
                onClickChangeFavorite = {viewModel.changeFavorite()}
            )
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isRefreshing = false,
                onRefresh = { viewModel.loadMod() }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {

                    state.mod?.let {
                        ButtonInstruction(navigator)
                        Spacer(Modifier.height(10.dp))
                        Preview(it)
                        Spacer(Modifier.height(20.dp))
                        TitleWithDescr(
                            mod = state.mod,
                            descriptionTextExpand = state.descriptionTextExpand,
                            descriptionImageExpand = state.descriptionImageExpand,
                            onClickSwitchDescriptionImage = viewModel::switchDescriptionImageExpand,
                            onClickSwitchDescriptionText = viewModel::switchDescriptionTextExpand
                        )
//                    Spacer(Modifier.height(10.dp))
//                    Metrics(it)
                        Spacer(Modifier.height(20.dp))
                        SupportVersions(mod = state.mod)
                        Spacer(Modifier.height(20.dp))
                        NativeApplovinView(Modifier.fillMaxWidth())
                        Spacer(Modifier.height(20.dp))
                        FileButtons(
                            mod = state.mod,
                            onClickMod = { viewModel.changeStageSetupMod(it) },
                            onClickAddonNotWork = { viewModel.openDontWorkDialog(true) },

                        )
                    }
                }

                Box(Modifier.fillMaxWidth(), Alignment.Center) {
                    when (loadModState) {
                        is DetailModState.LoadModState.Error -> {
                            CustomButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.retry)
                            ) { viewModel.loadMod() }
                        }

                        DetailModState.LoadModState.Loading -> {
                            Box(Modifier.padding(vertical = 20.dp)){
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
        state.mod?.let { mod ->
            state.choiceFilePathSetup?.let {
                SetupModDialog(
                    it.getModNameFromUrl(mod.category.toExtension()),
                    viewModel
                ) {
                    viewModel.changeStageSetupMod(null)
                }
            }
            if (state.dontWorkAddonDialogIsOpen) {
                DontWorkAddonDialog() { viewModel.openDontWorkDialog(false) }
            }
        }
    }

}


@Composable
private fun FileButtons(
    mod: ModEntity?,
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
                    text = it.getModNameFromUrl(mod.category.toExtension()),
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.primary.copy(0.4f),
                ) {
                    onClickMod(it)
                }
            }
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                text = stringResource(R.string.addon_don_t_work),
                contentColor = AppColors.WHITE,
                containerColor = Color(0xffE03131),
            ) {
                onClickAddonNotWork()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SupportVersions(
    mod: ModEntity?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.supported_versions),
            style = AppTypo.APP_TEXT,
            fontSize = 18.sp,
            color = AppColors.WHITE,
            fontFamily = AppFonts.SF.SEMI_BOLD,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            mod?.versions?.forEach { version ->
                SupportVersionItem(
                    value = version,
                )
            }
        }
    }
}

@Composable
private fun SupportVersionItem(value: String, actualVersion: Boolean = false) {
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
        fontFamily = AppFonts.SF.SEMI_BOLD,
    )
}

@Composable
private fun TitleWithDescr(
    mod: ModEntity?,
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
            fontFamily = AppFonts.SF.BOLD,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = if (descriptionTextExpand) mod.description else mod.description.take(300) + "...",
            style = AppTypo.APP_TEXT,
            fontSize = 14.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.SF.MEDIUM,
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
                        fontFamily = AppFonts.SF.BOLD,
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
                        fontFamily = AppFonts.SF.BOLD,
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
private fun Preview(mod: ModEntity) {
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
private fun ButtonInstruction(navigator: Navigator) {
    val instructionScreen = rememberScreen(SharedScreen.InstructionScreen)
    CustomButton(
        text = stringResource(R.string.instructions),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        navigator.push(instructionScreen)
    }
}

@Composable
private fun Header(
    mod: ModEntity?,
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
            fontFamily = AppFonts.SF.BOLD,
        )

        Image(
            modifier = Modifier
                .size(24.dp)
                .clickableEmpty { onClickChangeFavorite() },
            painter = painterResource(
                if (mod?.isFavorite ?: false) R.drawable.ic_mine_like_filled else R.drawable.ic_mine_like_stroke
            ),
            contentDescription = "favorite mods",
        )
    }
}