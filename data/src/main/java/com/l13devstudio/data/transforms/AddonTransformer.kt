package com.l13devstudio.data.transforms

import android.content.Context
import com.l13devstudio.data.source.local.database.CoreDatabase
import com.l13devstudio.data.source.remote.dto.addon.AddonDto
import com.l13devstudio.domain.entity.addon.AddonEntity

class AddonTransformer(context: Context) {

    private val recordAccess = CoreDatabase.getInstance(context).likeDao()

    suspend fun toEntity(dto: AddonDto) = AddonEntity(
        id = dto.id,
        type = dto.category,
        images = dto.descriptionImages.map { it.mapImageLink() },
        name = dto.title,
        desc = dto.description,
        preview = dto.image.mapImageLink(),
        files = dto.files,
        versions = dto.versions.map { it.version },
        isLike = recordAccess.getLike(dto.id)?.isActive ?: false
    )

    suspend fun toEntity(dtoList: List<AddonDto>) = dtoList.map { toEntity(it) }

    private fun String.mapImageLink() =
        if (this.take(2) == "/u") com.l13devstudio.data.BuildConfig.BASE_URL + this else this

}
