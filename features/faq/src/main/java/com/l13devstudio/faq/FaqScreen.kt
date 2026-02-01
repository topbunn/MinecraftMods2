package com.l13devstudio.faq

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.l13devstudio.faq.FaqType.SHARE_MOD
import com.l13devstudio.issue.IssueDialog
import com.l13devstudio.navigation.Destination
import com.l13devstudio.ui.R
import com.l13devstudio.ui.components.AppButton
import com.l13devstudio.ui.theme.AppTypo
import com.l13devstudio.ui.theme.LocalAppColors
import com.l13devstudio.ui.utils.appDropShadow

object FaqScreen: Tab, Screen {

    override val options: TabOptions
        @Composable get() = TabOptions(
            3U,
            stringResource(R.string.tabs_faq),
            painterResource(R.drawable.ic_nav_faq)
        )

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<FaqViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(12.dp, 20.dp),
        ) {
            TitleWithDesc()
            Spacer(Modifier.height(20.dp))
            FaqList(
                onClickSuggest = {
                    val suggestScreen = ScreenRegistry.get(Destination.SuggestScreen)
                    navigator.push(suggestScreen)
                }
            )
            Spacer(Modifier.height(20.dp))
            ButtonQuestion{ viewModel.openIssue(true) }
        }

        if (state.openIssue){
            IssueDialog { viewModel.openIssue(false) }
        }
    }

    @Composable
    private fun ButtonQuestion(onClick: () -> Unit) {
        AppButton(
            modifier = Modifier.fillMaxWidth().height(48.dp),
            text = stringResource(R.string.ask_a_question),
            onClick = onClick
        )
    }

    @Composable
    private fun FaqList(
        onClickSuggest: () -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            faqList.forEach {
                FaqItem(
                    faq = it,
                    content = when(it.typeQuestion){
                        SHARE_MOD -> {{
                            AppButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp),
                                text = stringResource(R.string.suggest),
                                onClick = onClickSuggest
                            )
                        }}
                        else -> null
                    }
                )
            }
        }
    }

    @Composable
    private fun FaqItem(
        faq: FaqModelUi,
        content: @Composable (() -> Unit)? = null
    ) {
        val colors = LocalAppColors.current
        var isExpand by rememberSaveable { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .appDropShadow(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .animateContentSize()
                .clip(RoundedCornerShape(8.dp))
                .background(colors.card)
                .clickable { isExpand = !isExpand }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(faq.questionRes),
                    style = AppTypo.button,
                    color = colors.title
                )

                val animateRotation by animateFloatAsState(targetValue = if (isExpand) 180f else 0f)
                Icon(
                    modifier = Modifier
                        .rotate(animateRotation),
                    painter = painterResource(R.drawable.ic_dropdown),
                    contentDescription = null,
                    tint = colors.primary
                )
            }
            if (isExpand){
                Box(Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.border))
                Text(
                    text = stringResource(faq.answerRes),
                    style = AppTypo.M1,
                    color = colors.text
                )
                content?.let { it() }
            }
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
    private fun Description() {
        val colors = LocalAppColors.current
        Text(
            text = stringResource(R.string.faq_desc),
            style = AppTypo.M1,
            color = colors.text
        )
    }

    @Composable
    private fun Title() {
        val colors = LocalAppColors.current
        Text(
            text = stringResource(R.string.tabs_faq),
            style = AppTypo.H1,
            fontSize = 24.sp,
            color = colors.title
        )
    }
}
