package com.hamit.domain.entity.addon

enum class AddonSortType {

    RELEVANCE,
    NAME,
    USED_COUNT,
    COMMENT_COUNTS,
    RATING;

    override fun toString() = when(this){
        com.hamit.domain.entity.addon.RELEVANCE -> "order"
        com.hamit.domain.entity.addon.NAME -> "name"
        com.hamit.domain.entity.addon.USED_COUNT -> "usedCount"
        com.hamit.domain.entity.addon.COMMENT_COUNTS -> "commentCounts"
        com.hamit.domain.entity.addon.RATING -> "rating"
    }
}