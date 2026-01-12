package com.hamit.addon

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.addon.components.AddonVersionList
import com.hamit.addon.components.Buttons
import com.hamit.addon.components.Description
import com.hamit.addon.components.Files
import com.hamit.addon.components.Gallery
import com.hamit.addon.components.Preview
import com.hamit.addon.components.Title
import kotlinx.parcelize.Parcelize
import org.koin.core.parameter.parametersOf

@Parcelize
data class AddonScreen(private val addonId: Int) : Screen, Parcelable {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<AddonViewModel>{ parametersOf(addonId) }
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 12.dp, bottom = 20.dp)
            ){
                state.addon?.let { addon ->
                    Preview(addon)
                    Spacer(Modifier.height(16.dp))
                    Title(addon)
                    Spacer(Modifier.height(10.dp))
                    AddonVersionList(addon)
                    Spacer(Modifier.height(16.dp))
                    Description(addon)
                    Spacer(Modifier.height(16.dp))
                    Gallery(addon)
                    Spacer(Modifier.height(16.dp))
                    Files(addon){}
                }
            }
            Buttons(
                addon = state.addon,
                onClickBack = { navigator.pop() },
                onClickLike = { viewModel.switchLikeStatus() }
            )
        }

    }
}
