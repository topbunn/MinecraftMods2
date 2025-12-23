package com.hamit.domain.entity.addon

enum class AddonType {

    WORLD,
    ADDON,
    TEXTURE_PACK,
    SKIN_PACK;

    fun toExtension(): String = when (this) {
        com.hamit.domain.entity.addon.ADDON -> ".mcaddon"
        com.hamit.domain.entity.addon.WORLD -> ".mcworld"
        com.hamit.domain.entity.addon.TEXTURE_PACK -> ".mcpack"
        com.hamit.domain.entity.addon.SKIN_PACK -> ".mcpack"
    }
}