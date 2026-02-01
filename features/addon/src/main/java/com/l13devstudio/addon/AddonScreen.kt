package com.l13devstudio.addon

import android.os.Parcelable
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.l13devstudio.addon.components.AddonVersionList
import com.l13devstudio.addon.components.Description
import com.l13devstudio.addon.components.Files
import com.l13devstudio.addon.components.Gallery
import com.l13devstudio.addon.components.OtherMods
import com.l13devstudio.addon.components.Preview
import com.l13devstudio.addon.components.SupportButtons
import com.l13devstudio.addon.components.Title
import com.l13devstudio.addon.components.TopbarButtons
import com.l13devstudio.android.ads.interstitial.InterstitialCoordinator
import com.l13devstudio.android.ads.natives.NativeCoordinator
import com.l13devstudio.domain.entity.AddonListStatusUi
import com.l13devstudio.domain.entity.AppExceptionType
import com.l13devstudio.domain.entity.AppExceptionType.Error
import com.l13devstudio.domain.entity.AppExceptionType.Maintenance
import com.l13devstudio.domain.entity.AppExceptionType.NoInternet
import com.l13devstudio.domain.entity.addon.AddonEntity
import com.l13devstudio.issue.IssueDialog
import com.l13devstudio.navigation.Destination
import com.l13devstudio.ui.R
import com.l13devstudio.ui.components.AppButton
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors
import kotlinx.parcelize.Parcelize
import org.koin.core.parameter.parametersOf

@Parcelize
data class AddonScreen(private val addonId: Int) : Screen, Parcelable {

    override val key: ScreenKey
        get() = addonId.toString()

    @Composable
    override fun Content() = key(addonId) {
        val activity = LocalActivity.currentOrThrow
        val viewModel = koinScreenModel<AddonViewModel> { parametersOf(addonId) }
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            InterstitialCoordinator.show(activity)
        }

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            isRefreshing = false,
            onRefresh = { viewModel.loadAddon() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 12.dp, bottom = 20.dp)
                    .navigationBarsPadding()
            ) {
                when (val status = state.loadStatus) {
                    is AddonListStatusUi.Error -> ErrorContent(status.type) { viewModel.loadAddon() }
                    AddonListStatusUi.Loading -> AddonShimmerContent()
                    AddonListStatusUi.Success -> {
                        state.addon?.let {
                            SuccessContent(
                                addon = it,
                                others = state.otherAddons,
                                textIsExpand = state.textIsExpand,
                                onClickNotWork = { viewModel.openIssue(true) },
                                onClickHowInstall = {
                                    val guideScreen = ScreenRegistry.get(Destination.GuideScreen)
                                    navigator.push(guideScreen)
                                },
                                onClickFiles = {
                                    val downloadScreen =
                                        ScreenRegistry.get(Destination.DownloadScreen(it))
                                    navigator.push(downloadScreen)
                                },
                                onClickOther = {
                                    val addonScreen =
                                        ScreenRegistry.get(Destination.AddonScreen(it))
                                    navigator.push(addonScreen)
                                }
                            ) { viewModel.switchTextExpand() }
                        }
                    }

                    else -> {}
                }
            }
            TopbarButtons(
                addon = state.addon,
                onClickBack = { navigator.pop() },
                onClickLike = { viewModel.switchLikeStatus() }
            )
        }
        if (state.openProblem) {
            IssueDialog {
                viewModel.openIssue(false)
            }
        }
    }

    @Composable
    private fun ErrorContent(type: AppExceptionType, onClickRetry: () -> Unit) {
        val colors = LocalAppColors.current
        val configuration = LocalConfiguration.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(configuration.screenHeightDp.dp)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val errorResId = when (type) {
                NoInternet -> R.string.error_no_internet
                Maintenance -> R.string.error_maintenance
                Error -> R.string.error_unknown
            }
            Text(
                text = stringResource(errorResId),
                textAlign = TextAlign.Center,
                style = AppTypo.M1,
                color = colors.text,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(20.dp))
            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                text = stringResource(R.string.retry),
                onClick = onClickRetry
            )
        }
    }

    @Composable
    private fun SuccessContent(
        addon: AddonEntity,
        others: List<AddonEntity>?,
        textIsExpand: Boolean,
        onClickOther: (id: Int) -> Unit,
        onClickNotWork: () -> Unit,
        onClickFiles: () -> Unit,
        onClickHowInstall: () -> Unit,
        onClickTextExpand: () -> Unit,
    ) {
        val colors = LocalAppColors.current
        Preview(addon)
        Spacer(Modifier.height(16.dp))
        Title(addon)
        Spacer(Modifier.height(10.dp))
        AddonVersionList(addon)
        Spacer(Modifier.height(16.dp))
        Description(addon, textIsExpand) { onClickTextExpand() }
        Spacer(Modifier.height(16.dp))
        NativeCoordinator.show(
            type = NativeCoordinator.ViewAdType.Native,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = colors.card),
        )
        Spacer(Modifier.height(16.dp))
        Gallery(addon)
        Spacer(Modifier.height(16.dp))
        Files(addon, onClickFiles)
        Spacer(Modifier.height(16.dp))
        SupportButtons(
            onClickHowInstall = onClickHowInstall,
            onClickNotWork = onClickNotWork
        )
        Spacer(Modifier.height(16.dp))
        OtherMods(others, onClickOther = onClickOther)
        Spacer(Modifier.height(16.dp))
        NativeCoordinator.show(
            type = NativeCoordinator.ViewAdType.Native,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = colors.card),
        )
    }
}
