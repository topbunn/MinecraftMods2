package com.hamit.download

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.android.play.core.review.ReviewManagerFactory
import com.hamit.android.utills.getModNameFromUrl
import com.hamit.domain.entity.DownloadFileStatus
import com.hamit.domain.entity.addon.AddonEntity
import com.hamit.domain.useCases.download.DownloadFileUseCase
import com.hamit.domain.useCases.file.FileExistsUseCase
import com.hamit.domain.useCases.file.OpenFileUseCase
import com.hamit.download.AddonFileUi.AddonFileUiStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.rustore.sdk.review.RuStoreReviewManagerFactory

class DownloadViewModel(
    private val addon: AddonEntity,
    private val fileExistsUseCase: FileExistsUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val openFileUseCase: OpenFileUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(DownloadState())
    val state = _state.asStateFlow()

    private val _events = Channel<DownloadEvent>(capacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val events get() = _events.receiveAsFlow()

    private val downloadJobs = mutableMapOf<String, Job>()

    private fun updateFileState(file: AddonFileUi, newStatus: AddonFileUiStatus) {
        _state.update { currentState ->
            val newFiles = currentState.files.map {
                if (it.name == file.name) {
                    it.copy(status = newStatus)
                } else {
                    it
                }
            }
            currentState.copy(files = newFiles)
        }
    }

    fun download(file: AddonFileUi) {
        if (downloadJobs.containsKey(file.name)) return

        val job = screenModelScope.launch {
            try {
                val result = downloadFileUseCase(file.link, file.name)

                updateFileState(file, AddonFileUiStatus.Downloading(0, 0, 0f))

                result.collect { downloadFileStatus ->
                    Log.d("DOWNLOAD_STATE", downloadFileStatus.toString())
                    val newStatus = when (downloadFileStatus) {
                        is DownloadFileStatus.Downloading -> {
                            AddonFileUiStatus.Downloading(
                                bytesDownloaded = downloadFileStatus.bytesDownloaded,
                                totalBytes = downloadFileStatus.totalBytes,
                                progress = downloadFileStatus.progress
                            )
                        }

                        DownloadFileStatus.Error -> {
                            _events.send(DownloadEvent.ShowError)
                            AddonFileUiStatus.NoSaved
                        }

                        DownloadFileStatus.Success -> {
                            val exists = fileExistsUseCase(file.name)
                            if (exists != null) {
                                AddonFileUiStatus.Saved(exists)
                            } else {
                                AddonFileUiStatus.NoSaved
                            }
                        }
                    }
                    updateFileState(file, newStatus)
                }
            } finally {
                downloadJobs.remove(file.name)
            }
        }
        downloadJobs[file.name] = job
    }

    fun showReview(activity: Activity) {
        if (BuildConfig.RUSTORE){
            showRuStoreReview(activity)
        } else {
            showGooglePlayReview(activity)
        }
    }

    private fun showRuStoreReview(activity: Activity){
        val manager = RuStoreReviewManagerFactory.create(activity.applicationContext)
        val request = manager.requestReviewFlow()

        request.addOnSuccessListener {
            val launch = manager.launchReviewFlow(it)
            launch.addOnFailureListener {
                Log.d("RUSTORE_REVIEW", "Ошибка launch: ${it.message}")
            }
        }
        request.addOnFailureListener {
            Log.d("RUSTORE_REVIEW", "Ошибка request: ${it.message}")
        }
    }

    private fun showGooglePlayReview(activity: Activity){
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(activity, reviewInfo)
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

    override fun onDispose() {
        downloadJobs.values.forEach { it.cancel() }
        downloadJobs.clear()
    }

}