package com.hamit.data.mappers

import android.content.Context
import com.hamit.data.api.dto.mods.EntryDto
import com.hamit.data.database.AppDatabase
import com.hamit.domain.entity.addon.AddonEntity

class EntityTransformer(context: Context) {

    private val recordAccess = AppDatabase.getInstance(context).recordAccess()

    suspend fun toEntity(dto: EntryDto) = AddonEntity(
        id = dto.id,
        category = dto.category,
        rating = dto.rating,
        commentCounts = dto.commentCounts,
        descriptionImages = dto.descriptionImages.map { it.mapImageLink() },
        title = dto.title,
        description = dto.description,
        image = dto.image.mapImageLink(),
        files = dto.files,
        versions = dto.versions.map { it.version },
        isLike = recordAccess.getRecord(dto.id)?.isActive ?: false
    )

    suspend fun toEntity(dtoList: List<EntryDto>) = dtoList.map { toEntity(it) }

    private fun String.mapImageLink() =
        if (this.take(2) == "/u") com.hamit.data.BuildConfig.BASE_URL + this else this

}
