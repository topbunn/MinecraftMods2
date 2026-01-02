package com.hamit.addon.dontWorkAddon

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.hamit.addon.dontWorkAddon.DontWorkAddonState.DontWorkScreenState
import com.hamit.ui.R
import com.hamit.ui.components.CustomButton
import com.hamit.ui.components.CustomTextField
import com.hamit.ui.components.ModalWrapper
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo

@Composable
fun Screen.DontWorkAddonDialog(
    onDismissDialog: () -> Unit
) {
    ModalWrapper(onDismissDialog) {
        val context = LocalContext.current
        val viewModel = koinScreenModel<DontWorkAddonViewModel>()
        val state by viewModel.state.collectAsState()
        val messageSent = stringResource(R.string.message_is_sent)

        LaunchedEffect(state.feedbackState) {
            when (val feedbackState = state.feedbackState) {
                is DontWorkScreenState.Error -> {
                    Toast.makeText(context, feedbackState.message, Toast.LENGTH_SHORT).show()
                }

                DontWorkScreenState.Success -> {
                    Toast.makeText(context, messageSent, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.describe_your_problem),
            style = AppTypo.APP_TEXT,
            fontSize = 16.sp,
            color = AppColors.GRAY,
            fontFamily = AppFonts.CORE.MEDIUM,
        )
        Spacer(Modifier.height(16.dp))
        CustomTextField(
            textValue = state.email,
            backgroundColor = AppColors.BLACK_BG,
            hint = stringResource(R.string.email),
            onTextChanged = { viewModel.changeEmail(it) }
        )
        Spacer(Modifier.height(10.dp))
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            verticalAlignment = Alignment.Top,
            isSingleLine = false,
            backgroundColor = AppColors.BLACK_BG,
            textValue = state.message,
            hint = stringResource(R.string.type_message),
            onTextChanged = { if (it.length < 1024) viewModel.changeMessage(it) }
        )
        Spacer(Modifier.height(16.dp))
        val buttonEnabled by viewModel.buttonEnabled.collectAsState()
        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            isEnabled = buttonEnabled,
            isLoading = state.feedbackState is DontWorkScreenState.Loading,
            label = stringResource(R.string.send)
        ) {
            viewModel.sendIssue()
        }
    }
}