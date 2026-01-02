package com.hamit.addon.installAddon

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.addon.AddonState.DownloadModState.Loading
import com.hamit.addon.AddonState.DownloadModState.Success
import com.hamit.addon.AddonViewModel
import com.hamit.android.utills.getModFile
import com.hamit.ui.R
import com.hamit.ui.components.CustomButton
import com.hamit.ui.components.ModalWrapper
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import java.io.File

@Composable
fun InstallAddonDialog(
    fileName: String,
    viewModel: AddonViewModel,
    onDismissDialog: () -> Unit,
) {
    val context = LocalContext.current
    val activity = LocalActivity.currentOrThrow
    val state by viewModel.state.collectAsState()
    var savedFile by rememberSaveable { mutableStateOf<File?>(null) }
    LaunchedEffect(fileName) { savedFile = getModFile(fileName) }
    LaunchedEffect(state.downloadState) {
        when (val downloadState = state.downloadState) {
            is Error -> Toast.makeText(context, downloadState.message, Toast.LENGTH_SHORT).show()
            Success -> savedFile = getModFile(fileName)
            else -> {}
        }
    }
    ModalWrapper(
        onDialogDismiss = onDismissDialog
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(
                if (savedFile != null) R.string.to_install_in_game_click_the_install else R.string.to_save_click_the_download_button
            ),
            style = AppTypo.APP_TEXT,
            fontSize = 16.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.MEDIUM,
        )
        Spacer(Modifier.height(16.dp))

        val downloadState = state.downloadState
        val downloadButtonText = stringResource(
            if (savedFile != null) R.string.install else R.string.download
        ).plus(if (downloadState is Loading) ": ${downloadState.progress} %" else "")
        CustomButton(
            label = downloadButtonText,
            isEnabled = downloadState !is Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            savedFile?.let {
                viewModel.installMod(context, it)
            } ?: run {
                viewModel.downloadFile()
                viewModel.showReview(activity)
            }
        }
        savedFile?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(stringResource(R.string.the_file_is_saved_the_path))
                    withStyle(SpanStyle(color = AppColors.BUTTON_RED)) {
                        append(it.path)
                    }
                },
                style = AppTypo.APP_TEXT,
                fontSize = 16.sp,
                color = AppColors.GRAY,
                fontFamily = AppFonts.CORE.MEDIUM,
            )
        }
    }
}