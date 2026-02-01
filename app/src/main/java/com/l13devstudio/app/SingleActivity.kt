package com.l13devstudio.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.l13devstudio.android.ads.interstitial.InterstitialCoordinator
import com.l13devstudio.android.ads.natives.NativeCoordinator
import com.l13devstudio.android.ads.open.OpenCoordinator
import com.l13devstudio.domain.useCases.config.ReceiveConfigUseCase
import com.l13devstudio.domain.useCases.region.ReceiveRegionUseCase
import com.l13devstudio.ui.utils.permissions
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
        InterstitialCoordinator.start(applicationContext)
    }

    override fun onStop() {
        super.onStop()
        InterstitialCoordinator.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        OpenCoordinator.destroy()
        NativeCoordinator.destroy()
        InterstitialCoordinator.destroy()
    }
}