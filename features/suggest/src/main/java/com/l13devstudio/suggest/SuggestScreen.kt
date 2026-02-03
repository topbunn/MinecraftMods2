package com.l13devstudio.suggest

import android.R.attr.required
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.l13devstudio.android.ads.natives.NativeCoordinator
import com.l13devstudio.suggest.SuggestState.FieldType.DESC
import com.l13devstudio.suggest.SuggestState.FieldType.entries
import com.l13devstudio.ui.R
import com.l13devstudio.ui.components.AppButton
import com.l13devstudio.ui.components.AppTextField
import com.l13devstudio.ui.theme.AppColors
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.utils.ObserveAsEvents
import com.l13devstudio.ui.utils.appDropShadow

object SuggestScreen : Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            2U,
            stringResource(R.string.tabs_suggest),
            painterResource(R.drawable.ic_nav_suggest)
        )

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(12.dp, 20.dp)
        ) {
            val colors = LocalAppColors.current
            TitleWithDesc()
            Spacer(Modifier.height(20.dp))
            Card()
            Spacer(Modifier.height(20.dp))
            NativeCoordinator.show(
                modifier = Modifier.fillMaxWidth()
                    .appDropShadow(RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = colors.card),
                type = NativeCoordinator.ViewAdType.Native,
            )
        }
    }

    @Composable
    private fun TitleWithDesc() {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Title()
            Spacer(Modifier.height(10.dp))
            Description()
        }
    }

    @Composable
    private fun Card() {
        val context = LocalContext.current
        val colors = LocalAppColors.current
        val viewModel = koinScreenModel<SuggestViewModel>()
        val state by viewModel.state.collectAsState()

        ObserveAsEvents(viewModel.events) {
            val messageRes = when(it){
                SuggestEvent.ShowError -> R.string.error_check_internet
                SuggestEvent.ShowSuccess -> R.string.suggest_submitted
            }
            Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
        }

        LaunchedEffect(Unit) {
            viewModel.handleChangeState()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .appDropShadow(RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(colors.card)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardFields(
                email = state.email,
                desc = state.desc,
                onChangeText = viewModel::changeFieldValue
            )
            Spacer(Modifier.height(10.dp))
            CardButton(
                isEnabled = state.submitEnabled,
                isLoading = state.isLoading,
            ){
                viewModel.submit()
            }
        }
    }

    @Composable
    private fun CardButton(
        isLoading: Boolean,
        isEnabled: Boolean,
        onClick: () -> Unit
    ) {
        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            text = stringResource(R.string.submit),
            isEnabled = isEnabled,
            isLoading = isLoading,
            onClick = onClick
        )
    }

    @Composable
    private fun CardFields(
        email: String,
        desc: String,
        onChangeText: (String, SuggestState.FieldType) -> Unit,
    ) {
        entries.forEach { type ->
            Field(
                type = type,
                value = when (type) {
//                    EMAIL -> email
                    DESC -> desc
                }
            ){ text ->
                onChangeText(text, type)
            }
        }
    }

    @Composable
    private fun Field(
        type: SuggestState.FieldType,
        value: String,
        onChangeText: (String) -> Unit
    ) {
        val colors = LocalAppColors.current
        val modifier =  Modifier.height(if (type == DESC) 120.dp else 48.dp )

        @Composable
        fun TipDesc(){
            Text(
                text = "Min ${value.length}/40",
                style = AppTypo.M1,
                color = if (value.length < 40) AppColors.RED else colors.text
            )
        }

        AppTextField(
            text = value,
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            singleLine = type != DESC,
            hint = stringResource(
                when(type){
//                    EMAIL -> R.string.hint_email
                    DESC -> R.string.hint_desc
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = when(type){
//                    EMAIL -> KeyboardType.Email
                    DESC -> KeyboardType.Text
                }
            ),
            leadingIconRes = when(type){
//                EMAIL -> R.drawable.ic_hint_mail
                DESC -> R.drawable.ic_hint_desc
            },
            tipContent = if (type == DESC) { { TipDesc() } } else null,
            onTextChange = onChangeText
        )
    }

    @Composable
    private fun Description() {
        val colors = LocalAppColors.current
        Text(
            text = stringResource(R.string.suggest_desc),
            style = AppTypo.M1,
            color = colors.text
        )
    }

    @Composable
    private fun Title() {
        val colors = LocalAppColors.current
        Text(
            text = stringResource(R.string.suggest),
            style = AppTypo.H1,
            fontSize = 24.sp,
            color = colors.title
        )
    }

}
