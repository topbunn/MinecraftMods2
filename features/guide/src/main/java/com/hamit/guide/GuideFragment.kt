package com.hamit.guide

import android.os.Parcelable
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.android.parcel.Parcelize

@Parcelize
class InstructionFragment(private val guideType: GuideType) : Screen, Parcelable {

    @Composable
    override fun Content() {

    }
}
