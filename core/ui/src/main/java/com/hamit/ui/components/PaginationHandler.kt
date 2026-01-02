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
import androidx.compose.ui.unit.sp
import com.hamit.ui.R
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

@Composable
fun PaginationHandler(
    isEndOfList: Boolean,
    isLoading: Boolean,
    isError: Boolean,
    isEmptyList: Boolean,
    loadKey: Any,
    onLoadMore: () -> Unit
) {
    when {
        isError -> {
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.retry)
            ) { onLoadMore() }
        }

        !isEndOfList && isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = AppColors.WHITE,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        !isEndOfList && !isLoading -> {
            LaunchedEffect(loadKey) {
                onLoadMore()
            }
        }

        !isLoading && !isError && isEmptyList -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.the_list_is_empty),
                    style = AppTypo.APP_TEXT,
                    fontSize = 18.sp,
                    color = AppColors.GRAY,
                    textAlign = TextAlign.Center,
                    fontFamily = AppFonts.CORE.BOLD,
                )
            }
        }
    }
}
