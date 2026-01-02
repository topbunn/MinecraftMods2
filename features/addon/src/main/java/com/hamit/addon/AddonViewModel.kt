package com.hamit.addon

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.android.play.core.review.ReviewManagerFactory
import com.hamit.addon.AddonState.DownloadModState
import com.hamit.addon.AddonState.DownloadModState.Idle
import com.hamit.addon.AddonState.LoadAddonState.Error
import com.hamit.addon.AddonState.LoadAddonState.Loading
import com.hamit.addon.AddonState.LoadAddonState.Success
import com.hamit.android.utills.getModNameFromUrl
import com.hamit.data.DataProcessState
import com.hamit.data.database.entity.LikeEntity
import com.hamit.data.repository.DataRepository
import com.hamit.ui.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.rustore.sdk.review.RuStoreReviewManagerFactory
import java.io.File

class AddonViewModel(
    private val addonId: Int,
    private val repo: DataRepository,
) : ScreenModel {

    private val _state = MutableStateFlow(AddonState())
    val state get() = _state.asStateFlow()

    init {
        loadAddon()
    }

    fun loadAddon() = screenModelScope.launch {
        _state.update { it.copy(addon = null, loadAddonState = Loading) }
        val result = repo.getMod(addonId)
        result.onSuccess { addon ->
            _state.update { it.copy(addon = addon, loadAddonState = Success) }
        }.onFailure {
            _state.update { it.copy(loadAddonState = Error("Loading error. Check internet connection")) }
        }

    }

    fun changePathFile(path: String?) = _state.update {
        it.copy(
            pathFile = path,
            downloadState = Idle
        )
    }

    fun switchImageExpand() = _state.update {
        it.copy(imageIsExpand = !state.value.imageIsExpand)
    }

    fun switchTextExpand() = _state.update {
        it.copy(textIsExpand = !state.value.textIsExpand)
    }

    fun shouldOpenIssue(value: Boolean) = _state.update {
        it.copy(shouldOpenIssue = value)
    }

    fun downloadFile() = screenModelScope.launch(CoroutineExceptionHandler { _, _ -> }) {
        _state.value.addon?.let { mod ->
            _state.value.pathFile?.let {
                val result =
                    repo.downloadFile(it, it.getModNameFromUrl(mod.category.toExtension()))
                result.onSuccess { downloadFlow ->
                    downloadFlow.collect {
                        val downloadState = when (val state = it) {
                            is DataProcessState.Running -> DownloadModState.Loading(state.progress)
                            is DataProcessState.Interrupted -> DownloadModState.Error("Download error. Check Internet connection")
                            DataProcessState.Completed -> DownloadModState.Success
                        }
                        _state.update { it.copy(downloadState = downloadState) }
                    }
                }.onFailure { error ->
                    _state.update { it.copy(downloadState = DownloadModState.Error("Download error. Check Internet connection")) }
                }
            }
        }
    }


    fun showReview(activity: Activity) {
        if (BuildConfig.RUSTORE) {
            showRuStoreReview(activity)
        } else {
            showGooglePlayReview(activity)
        }
    }

    private fun showRuStoreReview(activity: Activity) {
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

    private fun showGooglePlayReview(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(activity, reviewInfo)
            }
        }
    }

    fun installMod(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/octet-stream")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(
                context,
                context.getString(R.string.minecraft_is_not_installed),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun switchLikeStatus() = screenModelScope.launch {
        state.value.addon?.let { mod ->
            val newLike = LikeEntity(
                addonId = mod.id,
                isActive = !mod.isLike
            )
            repo.addLike(newLike)
            val newAddon = mod.copy(isLike = newLike.isActive)
            _state.update { it.copy(addon = newAddon) }
        }
    }

}