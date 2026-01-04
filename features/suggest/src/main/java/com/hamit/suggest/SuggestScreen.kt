package com.hamit.suggest

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hamit.ui.R
import com.hamit.ui.components.AppButton
import com.hamit.ui.components.AppTextField
import com.hamit.ui.theme.AppColors
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.appDropShadow

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
                .verticalScroll(rememberScrollState())
                .padding(12.dp, 20.dp)
        ) {
            TitleWithDesc()
            Spacer(Modifier.height(20.dp))
            Card()
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
        val colors = LocalAppColors.current
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
                email = "",
                onChangeEmail = {},
                link = "",
                onChangeLink = {},
                desc = "",
                onChangeDesc = {}
            )
            Spacer(Modifier.height(10.dp))
            CardButton(
                isEnabled = true,
                isLoading = false
            ){

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
        onChangeEmail: (String) -> Unit,

        link: String,
        onChangeLink: (String) -> Unit,

        desc: String,
        onChangeDesc: (String) -> Unit,
    ) {
        val colors = LocalAppColors.current
        AppTextField(
            text = email,
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(R.string.hint_email),
            leadingIconRes = R.drawable.ic_hint_mail,
            required = true,
            onTextChange = onChangeEmail
        )
        AppTextField(
            text = link,
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(R.string.hint_link),
            leadingIconRes = R.drawable.ic_hint_link,
            required = true,
            onTextChange = onChangeLink
        )
        AppTextField(
            text = desc,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            singleLine = false,
            hint = stringResource(R.string.hint_desc),
            leadingIconRes = R.drawable.ic_hint_desc,
            required = true,
            tipContent = {
                Text(
                    text = "Min ${desc.length}/40",
                    style = AppTypo.M1,
                    color = if (desc.length < 40) AppColors.RED else colors.text
                )
            },
            onTextChange = onChangeDesc
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
