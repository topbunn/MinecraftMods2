package com.hamit.loader

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.domain.entity.AppLogoRes
import com.hamit.navigation.Destination
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import org.koin.compose.koinInject

object LoaderScreen : Screen {


    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BLACK_BG)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            val applicationName = context.applicationInfo.labelRes
            val appLogoRes = koinInject<AppLogoRes>()

            Text(
                text = stringResource(applicationName),
                style = AppTypo.APP_TEXT,
                fontSize = 32.sp,
                fontFamily = AppFonts.CORE.BOLD,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(30.dp))
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(appLogoRes.res),
                contentDescription = "Image preview",
                contentScale = ContentScale.FillWidth
            )
            Spacer(Modifier.height(50.dp))
            ProgressBar()
        }
    }

    @Composable
    fun ProgressBar() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val activity = LocalActivity.currentOrThrow
            val navigator = LocalNavigator.currentOrThrow
            val viewModel = koinScreenModel<LoaderViewModel>()
            val state by viewModel.state.collectAsState()
            val tabsScreen = rememberScreen(Destination.DashboardScreen)

            LaunchedEffect(state.adInit) {
                if (state.adInit){
                    InterstitialCoordinator.setCallback {
                        viewModel.navigateToApp()
                    }
                }
            }

            LaunchedEffect(state.shouldGoApp) {
                if (state.shouldGoApp) {
                    viewModel.navigateToApp()
                }
            }

            LaunchedEffect(state.toNavigate) {
                if (state.toNavigate) {
                    InterstitialCoordinator.deleteCallback()
                    InterstitialCoordinator.show(activity)
                    navigator.replaceAll(tabsScreen)
                }
            }

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }


}