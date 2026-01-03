package com.hamit.addon

import android.os.Parcelable
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddonScreen(private val addonId: Int) : Screen, Parcelable {

    @Composable
    override fun Content() {

    }

}
