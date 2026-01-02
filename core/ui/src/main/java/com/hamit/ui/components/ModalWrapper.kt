package com.hamit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hamit.ui.theme.AppColors
import com.hamit.android.ads.natives.NativeCoordinator

@Composable
fun ModalWrapper(
    onDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier
        .heightIn(max = 700.dp)
        .padding(horizontal = 20.dp)
        .background(color = AppColors.GRAY_BG, RoundedCornerShape(8.dp))
        .padding(horizontal = 16.dp, vertical = 20.dp)
        .clipToBounds(),
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDialogDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        val localContext = LocalContext.current

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                content = content
            )

            Box(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                NativeCoordinator.show(
                    Modifier.fillMaxWidth()
                )
            }
        }
    }
}
