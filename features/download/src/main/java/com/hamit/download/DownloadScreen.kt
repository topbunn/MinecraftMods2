package com.hamit.download

import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hamit.android.utills.bytesToMb
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.ui.R
import com.hamit.ui.components.AppButton
import com.hamit.ui.theme.AppFonts
import com.hamit.ui.theme.AppTypo
import com.hamit.ui.theme.LocalAppColors
import com.hamit.ui.utils.ObserveAsEvents
import com.hamit.ui.utils.appDropShadow
import kotlinx.parcelize.Parcelize
import org.koin.core.parameter.parametersOf
import java.util.Locale

@Parcelize
class DownloadScreen(
    private val addon: AddonEntity
): Screen, Parcelable {


    @Composable
    override fun Content() {
        val context = LocalContext.current
        val colors = LocalAppColors.current
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<DownloadViewModel>{ parametersOf(addon) }
        val state by viewModel.state.collectAsState()

        val errorMessage = stringResource(R.string.error_download_mod)
        ObserveAsEvents(viewModel.events) {
            when(it){
                is DownloadEvent.ShowError -> {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Header{ navigator.pop() }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = stringResource(R.string.download_file),
                fontFamily = AppFonts.CORE,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = colors.text
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
            ) {
                items(items = state.files, key = { it.link }){ file ->
                    FileItem(
                        file = file,
                        onClickDownload = { viewModel.download(file) },
                        onClickInstall = { viewModel.openFile(file) },
                        onClickDirectory = { viewModel.openDownloads(context) },
                    )
                }
            }
        }
    }

    @Composable
    private fun FileItem(
        file: AddonFileUi,
        onClickDownload: () -> Unit,
        onClickInstall: () -> Unit,
        onClickDirectory: () -> Unit,
    ) {
        val colors = LocalAppColors.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .appDropShadow(RoundedCornerShape(24.dp), 2.dp, alpha = 0.1f)
                .clip(RoundedCornerShape(24.dp))
                .background(colors.card)
                .padding(20.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = file.name,
                color = colors.title,
                style = AppTypo.H3,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            when(val status = file.status){
                AddonFileUi.AddonFileUiStatus.NoSaved -> FileItemNoSaved(
                    onClickDownload = onClickDownload
                )
                is AddonFileUi.AddonFileUiStatus.Downloading -> FileItemDownloading(
                    progress = status.progress,
                    downloadedBytes = status.bytesDownloaded
                )
                is AddonFileUi.AddonFileUiStatus.Saved -> FileItemSaved(
                    path = status.path,
                    onClickInstall = onClickInstall,
                    onClickDirectory = onClickDirectory
                )
            }
        }
    }

    @Composable
    private fun FileItemSaved(
        path: String,
        onClickInstall: () -> Unit,
        onClickDirectory: () -> Unit
    ) {
        val colors = LocalAppColors.current
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            AppButton(
                text = stringResource(R.string.install),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) { onClickInstall() }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.card)
                    .border(2.dp, colors.primary, RoundedCornerShape(10.dp))
                    .clickable { onClickDirectory() }
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_directory),
                    contentDescription = null,
                    tint = colors.primary
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.file_saved_to_storage))
                withStyle(
                    SpanStyle(color = colors.primary)
                ){
                    append(" $path")
                }
            },
            fontFamily = AppFonts.CORE,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            color = colors.text
        )
    }

    @Composable
    private fun FileItemDownloading(progress: Float, downloadedBytes: Long) {
        val colors = LocalAppColors.current
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                    append(stringResource(R.string.downloading))
                    withStyle(SpanStyle(color = colors.primary, fontWeight = FontWeight.SemiBold)){
                        val percent = (progress * 100).toInt().toString() + "%"
                        val downloadedMb = String.format(Locale.US, "%.2f", downloadedBytes.bytesToMb())
                        append(" $percent ($downloadedMb MB)")
                    }
                },
            color = colors.text,
            fontFamily = AppFonts.CORE,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (progress != 0f){
                    Box(
                        Modifier
                            .weight(progress)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(colors.primary)
                    )
                }
                if ((1 - progress) != 0f){
                    Box(
                        Modifier
                            .weight(1 - progress)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(colors.background)
                    )
                }
            }
            Box(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(2.dp)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(colors.primary)
            )
        }
    }

    @Composable
    private fun FileItemNoSaved(onClickDownload: () -> Unit) {
        AppButton(
            text = stringResource(R.string.download),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { onClickDownload() }
    }

    @Composable
    private fun Header(
        onClickBack: () -> Unit
    ) {
        val colors = LocalAppColors.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .appDropShadow(CircleShape)
                    .clip(CircleShape)
                    .background(colors.card)
                    .clickable(onClick = onClickBack)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = colors.primary
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.install),
                style = AppTypo.H2,
                color = colors.title
            )
        }
    }

}