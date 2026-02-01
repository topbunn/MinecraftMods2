package com.l13devstudio.loader

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.l13devstudio.android.ads.natives.NativeCoordinator
import com.l13devstudio.domain.entity.AppLogoRes
import com.l13devstudio.ui.components.AppButton
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.utils.ObserveAsEvents
import com.l13devstudio.ui.utils.appDropShadow
import org.koin.compose.koinInject

object LoaderScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<LoaderViewModel>()
        val state by viewModel.state.collectAsState()

        ObserveAsEvents(viewModel.events) {
            when(it){
                is LoaderEvent.OpenScreen -> {
                    val screen = ScreenRegistry.get(it.destination)
                    navigator.replaceAll(screen)
                }
            }
        }


        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Logo(preloadCompleted = state.nativePreloadComplete)
            if(!state.nativePreloadComplete) Spacer(Modifier.height(80.dp))
            Card(preloadCompleted = state.nativePreloadComplete){
                viewModel.navigateToDashboard()
            }
        }
    }

    @Composable
    private fun Card(preloadCompleted: Boolean, onClick: () -> Unit) {
        val colors = LocalAppColors.current
        val context = LocalContext.current
        val applicationName = context.applicationInfo.labelRes
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .appDropShadow()
                .clip(RoundedCornerShape(32.dp))
                .background(colors.card)
                .padding(top = 48.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val appLogoRes = koinInject<AppLogoRes>()
                val animateSize by animateDpAsState(
                    targetValue = if (preloadCompleted) 80.dp else 0.dp,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
                Image(
                    modifier = Modifier
                        .size(animateSize)
                        .clip(RoundedCornerShape(8.dp)),
                    painter = painterResource(appLogoRes.res),
                    contentDescription = "Logo application",
                    contentScale = ContentScale.FillWidth
                )

                val animateFontSize by animateIntAsState(
                    targetValue = if (preloadCompleted) 24 else 32,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
                Text(
                    text = stringResource(applicationName),
                    style = AppTypo.H1,
                    color = colors.title,
                    fontSize = animateFontSize.sp
                )
            }
            if (preloadCompleted){
                val colors = LocalAppColors.current
                val animateHeightButton by animateDpAsState(
                    targetValue = if (preloadCompleted) 48.dp else 0.dp,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
                AppButton(
                    modifier = Modifier.fillMaxWidth().height(animateHeightButton).padding(horizontal = 12.dp),
                    text = stringResource(com.l13devstudio.ui.R.string.start),
                    onClick = onClick,
                    afterTextContent = {
                        Icon(
                            painter = painterResource(com.l13devstudio.ui.R.drawable.ic_start),
                            contentDescription = stringResource(com.l13devstudio.ui.R.string.start),
                            tint = colors.onPrimary
                        )
                    }
                )
            } else {
                Column {
                    Spacer(Modifier.height(24.dp))
                    PulseLoader(
                        size = 38.dp,
                        color = colors.primary
                    )
                }
            }
            val additionalModifiers = if (preloadCompleted) Modifier.wrapContentHeight() else Modifier.height(0.dp)
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
//                        .appDropShadow(RoundedCornerShape(24.dp))
//                        .clip(RoundedCornerShape(24.dp))
                    .background(colors.card)
            ){
                NativeCoordinator.show(
                    type = NativeCoordinator.ViewAdType.Native,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(additionalModifiers)
                )
            }
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
    private fun Logo(preloadCompleted: Boolean) {
        val appLogoRes = koinInject<AppLogoRes>()
        var imageWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        val imageHeight by animateDpAsState(
            targetValue = if (preloadCompleted) 0.dp else imageWidth,
            animationSpec = tween(
                durationMillis = if (preloadCompleted) 1000 else 0,
                easing = FastOutSlowInEasing
            )
        )

        val animateAlpha by animateFloatAsState(
            targetValue = if (preloadCompleted) 0f else 1f,
            animationSpec = tween(
                durationMillis = 900,
                easing = FastOutSlowInEasing
            )
        )

        val additionalModifier = if (preloadCompleted) Modifier.height(imageHeight) else Modifier.aspectRatio(1f)

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .then(additionalModifier)
                .alpha(animateAlpha)
                .appDropShadow()
                .clip(RoundedCornerShape(32.dp))
                .onSizeChanged { size ->
                    imageWidth = with(density) { size.width.toDp() }
                },
            painter = painterResource(appLogoRes.res),
            contentDescription = "Logo application",
            contentScale = ContentScale.FillWidth
        )
    }


}