package com.hamit.download

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.hamit.android.utills.getModNameFromUrl
import com.hamit.domain.entity.DownloadFileStatus
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.domain.useCases.download.DownloadFileUseCase
import com.hamit.domain.useCases.file.FileExistsUseCase
import com.hamit.domain.useCases.file.OpenFileUseCase
import com.hamit.download.AddonFileUi.AddonFileUiStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val addon: AddonEntity,
    private val fileExistsUseCase: FileExistsUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val openFileUseCase: OpenFileUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(DownloadState())
    val state = _state.asStateFlow()

    fun download(file: AddonFileUi) = screenModelScope.launch {
        val result = downloadFileUseCase(file.link, file.name)
        result.collect { downloadFileStatus ->
            _state.update {
                val newStatus = when (val downloadFileStatus = downloadFileStatus) {
                    is DownloadFileStatus.Downloading -> {
                        AddonFileUiStatus.Downloading(
                            bytesDownloaded = downloadFileStatus.bytesDownloaded,
                            totalBytes = downloadFileStatus.totalBytes,
                            progress = downloadFileStatus.progress
                        )
                    }

                    DownloadFileStatus.Error -> {
                        AddonFileUiStatus.NoSaved
                    }

                    DownloadFileStatus.Success -> {
                        val exists = fileExistsUseCase(file.name)
                        if (exists != null){
                            AddonFileUiStatus.Saved(exists)
                        } else {
                            AddonFileUiStatus.NoSaved
                        }
                    }

                }
                val newFiles = it.files.toMutableList().map {
                    if (it.name == file.name) {
                        it.copy(status = newStatus)
                    } else { it }
                }
                it.copy(files = newFiles)
            }

        }
    }

    fun openFile(file: AddonFileUi) = screenModelScope.launch {
        openFileUseCase(file.name)
    }

    private fun loadFiles() = screenModelScope.launch {
        val files = addon.files.map { url ->
            val name = url.getModNameFromUrl(addon.type.toExtension())
            val exists = fileExistsUseCase(name)

            AddonFileUi(
                name = name,
                link = url,
                status = if (exists != null) AddonFileUiStatus.Saved(exists) else AddonFileUiStatus.NoSaved
            )
        }

        _state.update { it.copy(files = files) }
    }

    fun openDownloads(context: Context) {
        val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        context.startActivity(intent)
    }

    init {
        loadFiles()
    }

}