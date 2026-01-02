package com.hamit.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.android.ads.open.OpenCoordinator
import com.hamit.data.repository.RegionRepositoryImpl
import com.hamit.data.repository.AddonRepositoryImpl
import com.hamit.domain.useCases.config.ReceiveConfigUseCase
import com.hamit.domain.useCases.region.ReceiveRegionUseCase
import com.hamit.ui.Root
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.colorScheme
import com.hamit.ui.utils.permissions

class SingleActivity : ComponentActivity() {

    private lateinit var receiveConfigUseCase: ReceiveConfigUseCase
    private lateinit var receiveRegionUseCase: ReceiveRegionUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveConfigUseCase = getKoin().get()
        receiveRegionUseCase = getKoin().get()
        initialAppOpenAd()
        enableEdgeToEdge()
        setContent {
            permissions(Manifest.permission.POST_NOTIFICATIONS)
            MaterialTheme(colorScheme.copy(primary = AppColors.PRIMARY)) {
                Root()
            }
        }
    }

    private fun initialAppOpenAd() = lifecycleScope.launch{
        val config = receiveConfigUseCase()
        val location = receiveRegionUseCase()
        OpenCoordinator.init(this@SingleActivity, location, config)
    }

    override fun onStart() {
        super.onStart()
        OpenCoordinator.start(this)
        InterstitialCoordinator.start()
    }

    override fun onStop() {
        super.onStop()
        OpenCoordinator.stop()
        InterstitialCoordinator.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        OpenCoordinator.destroy()
        InterstitialCoordinator.destroy()
        NativeCoordinator.destroy()
    }


}