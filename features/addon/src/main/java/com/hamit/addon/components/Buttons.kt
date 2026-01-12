package com.hamit.addon.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.components.AppIconButton

@Composable
internal fun BoxScope.Buttons(
    addon: AddonEntity?,
    onClickBack: () -> Unit,
    onClickLike: () -> Unit,
) {
    AppIconButton(
        painter = painterResource(R.drawable.ic_back),
        modifier = Modifier
            .align(Alignment.TopStart)
            .padding(20.dp)
    ) { onClickBack() }
    addon?.let { addon ->
        AppIconButton(
            painter = painterResource(R.drawable.ic_like_filled.takeIf { addon.isLike } ?: R.drawable.ic_like_stroke),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
        ) { onClickLike() }
    }
}