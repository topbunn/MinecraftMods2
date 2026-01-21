package com.hamit.loader

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.domain.entity.AppLogoRes
import com.hamit.navigation.Destination
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.ObserveAsEvents
import com.hamit.ui.utils.appDropShadow
import org.koin.compose.koinInject

object LoaderScreen : Screen {

    @Composable
    override fun Content() {
        val activity = LocalActivity.currentOrThrow
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<LoaderViewModel>()
        val dashboardScreen = rememberScreen(Destination.DashboardScreen)

        ObserveAsEvents(viewModel.events) {
            when(it){
                is LoaderEvent.OpenDashboard -> {
                    InterstitialCoordinator.show(activity)
                    navigator.replaceAll(dashboardScreen)
                }
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Logo()
            Card()
        }
    }

    @Composable
    private fun Card() {
        val colors = LocalAppColors.current
        val context = LocalContext.current
        val applicationName = context.applicationInfo.labelRes
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .appDropShadow()
                .clip(RoundedCornerShape(32.dp))
                .background(colors.card)
                .padding(start = 24.dp, top = 48.dp, end = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(applicationName),
                style = AppTypo.H1,
                color = colors.title,
            )
            PulseLoader(
                size = 38.dp,
                color = colors.primary
            )
        }
    }

    @Composable
    private fun PulseLoader(
        size: Dp,
        color: Color,
        shape: Shape = CircleShape,
        count: Int = 2,
        durationMillis: Int = 1500,
        targetScale: Float = 2.5f,
        initialScale: Float = 0.5f,
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ){
            repeat(count){
                val delayPulse = (it - 1) * (durationMillis / count)
                val animationAlpha by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                        initialStartOffset = StartOffset(delayPulse)
                    )
                )
                val animationScale by infiniteTransition.animateFloat(
                    initialValue = initialScale,
                    targetValue = targetScale,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                        initialStartOffset = StartOffset(delayPulse)
                    )
                )
                Box(
                    modifier = Modifier.fillMaxSize()
                        .scale(animationScale)
                        .alpha(animationAlpha)
                        .background(color, shape)
                )
            }
        }
    }

    @Composable
    private fun Logo() {
        val appLogoRes = koinInject<AppLogoRes>()
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .appDropShadow()
                .clip(RoundedCornerShape(32.dp)),
            painter = painterResource(appLogoRes.res),
            contentDescription = "Logo application"
        )
    }


}