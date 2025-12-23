package com.hamit.main

import com.hamit.domain.entity.addon.AddonType
import com.hamit.ui.R

enum class ModTypeUi(val titleRes: Int) {

    ALL(R.string.sort_type_enum_all),
    ADDON(R.string.sort_type_enum_addon),
    MAPS(R.string.sort_type_enum_maps),
    TEXTURE(R.string.sort_type_enum_texture),
    SKINS(R.string.sort_type_enum_skins);

    fun toModSortType() = when(this){
        ModTypeUi.ALL -> null
        ModTypeUi.ADDON -> AddonType.ADDON
        ModTypeUi.MAPS -> AddonType.WORLD
        ModTypeUi.TEXTURE -> AddonType.TEXTURE_PACK
        ModTypeUi.SKINS -> AddonType.SKIN_PACK
    }

}