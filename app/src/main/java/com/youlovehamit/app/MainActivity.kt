package com.youlovehamit.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import com.hamit.android.ad.inter.InterAdInitializer
import com.hamit.android.ad.natives.NativeAdCoordinator
import com.hamit.android.ad.open.OpenAdInitializer
import com.hamit.data.repository.LocationRepository
import com.hamit.data.repository.ModRepository
import com.hamit.ui.Root
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.colorScheme
import com.hamit.ui.utils.permissions

class MainActivity : ComponentActivity() {

    private lateinit var modRepository: ModRepository
    private lateinit var locationRepository: LocationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modRepository = getKoin().get()
        locationRepository = getKoin().get()
        initOpenAd()
        enableEdgeToEdge()
        setContent {
            permissions(Manifest.permission.POST_NOTIFICATIONS)
            MaterialTheme(colorScheme.copy(primary = AppColors.PRIMARY)) {
                Root()
            }
        }
    }

    private fun initOpenAd() = lifecycleScope.launch{
        val config = modRepository.getConfig()
        val location = locationRepository.getLocation()
        OpenAdInitializer.init(this@MainActivity, location, config)
    }

    override fun onStart() {
        super.onStart()
        OpenAdInitializer.onStart(this)
        InterAdInitializer.onStart()
    }

    override fun onStop() {
        super.onStop()
        OpenAdInitializer.onStop()
        InterAdInitializer.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        OpenAdInitializer.onDestroy()
        InterAdInitializer.onDestroy()

        NativeAdCoordinator.onDestroy()

    }


}