package com.hamit.ui.entity

import com.hamit.domain.entity.addon.AddonSortType
import com.hamit.ui.R

enum class AddonSortTypeUi(
    val stringResourceId: Int
) {

    RELEVANCE(R.string.sort_enum_relevance),
    USED_COUNT(R.string.sort_enum_best),
    RATING(R.string.sort_enum_rating);

    fun toAddonSortType() = when(this){
        RELEVANCE -> AddonSortType.RELEVANCE
        USED_COUNT -> AddonSortType.COMMENT_COUNTS
        RATING -> AddonSortType.RATING
    }
}