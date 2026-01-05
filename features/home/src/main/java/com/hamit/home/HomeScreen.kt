package com.hamit.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.ui.R
import com.hamit.ui.components.AppTextField
import com.hamit.ui.theme.LocalAppColors
import okhttp3.internal.http2.Header

object HomeScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            0U,
            stringResource(R.string.tabs_main),
            painterResource(R.drawable.ic_nav_main)
        )

    @Composable
    override fun Content() {
        Column{
            Header()
        }
    }

    @Composable
    private fun Header() {
        val colors = LocalAppColors.current
        Row(
            modifier = Modifier.fillMaxWidth().background(colors.card).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var text by remember { mutableStateOf("") }
            AppTextField(
                modifier = Modifier.weight(1f).height(52.dp),
                text = text,
                fontSize = 18.sp,
                paddingValues = PaddingValues(start = 17.dp, top = 17.dp, bottom = 17.dp, end = 12.dp),
                hint = stringResource(R.string.search),
                leadingIconRes = R.drawable.ic_search
            ) {
                text = it
            }
            IconButton(
                modifier = Modifier.size(52.dp)
                    .border(2.dp, colors.border, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_filters),
                    contentDescription = null,
                    tint = colors.primary
                )
            }
        }
    }

}