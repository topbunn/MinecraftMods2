package com.l13devstudio.ui.components.addon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l13devstudio.domain.entity.AddonListStatusUi
import com.l13devstudio.domain.entity.AppExceptionType
import com.l13devstudio.ui.R
import com.l13devstudio.ui.components.AppButton
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalColors

internal fun LazyListScope.AddonListFooter(
    colors: LocalColors,
    status: AddonListStatusUi,
    isEndOfList: Boolean,
    isEmpty: Boolean,
    onRetry: () -> Unit
) {

    when {
        status is AddonListStatusUi.Error -> item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                val errorResId = when (status.type) {
                    AppExceptionType.NoInternet -> R.string.error_no_internet
                    AppExceptionType.Maintenance -> R.string.error_maintenance
                    AppExceptionType.Error -> R.string.error_unknown
                }
                Text(
                    text = stringResource(errorResId),
                    textAlign = TextAlign.Center,
                    style = AppTypo.M1,
                    color = colors.text,
                    fontSize = 18.sp
                )
                AppButton(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    text = stringResource(R.string.retry),
                    onClick = onRetry
                )
            }
        }

        status == AddonListStatusUi.Loading && !isEndOfList -> item {
            CircularProgressIndicator(
                color = colors.text,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(24.dp)
            )
        }

        isEmpty && status == AddonListStatusUi.Success -> item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.the_list_is_empty),
                    textAlign = TextAlign.Center,
                    style = AppTypo.M1,
                    color = colors.text,
                    fontSize = 16.sp
                )
            }
        }
    }
}
