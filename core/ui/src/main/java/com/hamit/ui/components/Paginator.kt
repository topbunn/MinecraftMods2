package com.hamit.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hamit.ui.R
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors

@Composable
fun PaginationLoader(
    isEndOfList: Boolean,
    isLoad: Boolean,
    isError: Boolean,
    listIsEmpty: Boolean,
    loadKey: Any,
    onPreload: () -> Unit
) {
    val colors = LocalAppColors.current
    when {
        isError -> {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.retry)
            ) { onPreload() }
        }

        !isEndOfList && isLoad -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colors.primary,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        !isEndOfList && !isLoad -> {
            LaunchedEffect(loadKey) {
                onPreload()
            }
        }

        !isLoad && !isError && listIsEmpty -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.the_list_is_empty),
                    style = AppTypo.H2,
                    color = colors.title,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}