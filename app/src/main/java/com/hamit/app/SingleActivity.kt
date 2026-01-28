package com.hamit.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.hamit.android.ads.interstitial.InterstitialCoordinator
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.android.ads.open.OpenCoordinator
import com.hamit.domain.useCases.config.ReceiveConfigUseCase
import com.hamit.domain.useCases.region.ReceiveRegionUseCase
import com.hamit.ui.Root
import com.hamit.ui.utils.permissions
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin

class SingleActivity : ComponentActivity() {

    private lateinit var receiveConfigUseCase: ReceiveConfigUseCase
    private lateinit var receiveRegionUseCase: ReceiveRegionUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveConfigUseCase = getKoin().get()
        receiveRegionUseCase = getKoin().get()
        initialNativeAd()
        initialInterAd()
        initialAppOpenAd()
        enableEdgeToEdge()
        setContent {
            permissions(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            Root()
        }
    }

    private fun initialAppOpenAd() = lifecycleScope.launch {
        val config = receiveConfigUseCase()
        val location = receiveRegionUseCase()
        OpenCoordinator.init(this@SingleActivity, location, config)
    }

    private fun initialNativeAd() = lifecycleScope.launch {
        val config = receiveConfigUseCase()
        val location = receiveRegionUseCase()
        NativeCoordinator.init(this@SingleActivity, location, config)
    }

    private fun initialInterAd() = lifecycleScope.launch {
        val config = receiveConfigUseCase()
        val location = receiveRegionUseCase()
        InterstitialCoordinator.init(this@SingleActivity, location, config)
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
        NativeCoordinator.destroy()
        InterstitialCoordinator.destroy()
    }
}