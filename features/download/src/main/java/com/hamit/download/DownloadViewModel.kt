package com.hamit.download

import android.R.attr.name
import android.system.Os.link
import cafe.adriel.voyager.core.model.ScreenModel
import com.hamit.android.utills.getModNameFromUrl
import com.hamit.domain.entity.addon.AddonEntity

class DownloadViewModel(private val addon: AddonEntity): ScreenModel {

    private fun mapToAddonFilesUi(files: List<String>) = files.map {
        AddonFileUi(
            name = it.getModNameFromUrl(addon.type.toExtension()),
            link = it,
            status = TODO()
        )
    }

}