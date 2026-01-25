package com.hamit.addon.issue

import android.R.attr.onClick
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.hamit.addon.AddonViewModel
import com.hamit.android.ads.natives.NativeCoordinator
import com.hamit.ui.R
import com.hamit.ui.components.AppButton
import com.hamit.ui.components.AppTextField
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.ObserveAsEvents
import com.hamit.ui.utils.appDropShadow

@Composable
fun Screen.IssueDialog(
    onDismiss: () -> Unit
) = Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
){
    val colors = LocalAppColors.current
    val context = LocalContext.current
    val viewModel = koinScreenModel<IssueViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) {
        val messageRes = when(it){
            IssueEvent.ShowError -> R.string.error_check_internet
            IssueEvent.ShowSuccess -> R.string.suggest_submitted
        }
        Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
        if (IssueEvent.ShowSuccess == it){
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleChangeState()
    }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(colors.card)
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.feedback),
                color = colors.title,
                fontFamily = AppFonts.CORE,
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.describe_problem),
                color = colors.text,
                fontFamily = AppFonts.CORE,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                text = state.email,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                singleLine = true,
                hint = stringResource(R.string.hint_email),
                leadingIconRes = R.drawable.ic_hint_mail,
                required = true,
                onTextChange = { viewModel.changeFieldValue(it, IssueState.FieldType.EMAIL) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppTextField(
                text = state.desc,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false,
                hint = stringResource(R.string.hint_desc),
                leadingIconRes = R.drawable.ic_hint_mail,
                required = true,
                onTextChange = { viewModel.changeFieldValue(it, IssueState.FieldType.DESC) },
                tipContent = {
                    Text(
                        text = "Min ${state.desc.length}/40",
                        style = AppTypo.M1,
                        color = if (state.desc.length < 40) AppColors.RED else colors.text
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                text = stringResource(R.string.submit),
                isEnabled = state.submitEnabled,
                isLoading = state.isLoading,
                onClick = { viewModel.submit() }
            )
        }
        Spacer(Modifier.height(10.dp))
        NativeCoordinator.show(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .appDropShadow(RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(color = colors.card),
            type = NativeCoordinator.ViewAdType.Native,
        )
    }
}