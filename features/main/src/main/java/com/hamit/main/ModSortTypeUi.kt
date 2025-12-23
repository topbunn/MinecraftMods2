package com.hamit.main

import com.hamit.domain.entity.addon.AddonSortType
import com.hamit.ui.R

enum class ModSortTypeUi(
    val stringRes: Int
) {

    RELEVANCE(R.string.sort_enum_relevance),
    USED_COUNT(R.string.sort_enum_best),
    RATING(R.string.sort_enum_rating);

    fun toModSortType() = when(this){
        ModSortTypeUi.RELEVANCE -> AddonSortType.RELEVANCE
        ModSortTypeUi.USED_COUNT -> AddonSortType.COMMENT_COUNTS
        ModSortTypeUi.RATING -> AddonSortType.RATING
    }
}