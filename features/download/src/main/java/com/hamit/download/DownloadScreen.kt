package com.hamit.download

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow
import kotlinx.parcelize.Parcelize

@Parcelize
class DownloadScreen(
    private val addon: AddonEntity
): Screen, Parcelable {


    @Composable
    override fun Content() {
        val colors = LocalAppColors.current
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier.fillMaxSize()
                .statusBarsPadding()
                .padding(vertical = 20.dp)
        ) {
            Header{ navigator.pop() }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = stringResource(com.hamit.ui.R.string.install),
                        fontFamily = AppFonts.CORE,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = colors.text
                    )
                }
                items(items = files, key = {it}){

                }
            }
        }
    }

    @Composable
    private fun Header(
        onClickBack: () -> Unit
    ) {
        val colors = LocalAppColors.current
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(40.dp)
                    .clip(CircleShape)
                    .background(colors.card)
                    .appDropShadow(CircleShape)
                    .clickable(onClick = onClickBack)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(com.hamit.ui.R.drawable.ic_back),
                    contentDescription = null,
                    tint = colors.primary
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = stringResource(com.hamit.ui.R.string.install),
                style = AppTypo.H2,
                color = colors.title
            )
        }
    }

}