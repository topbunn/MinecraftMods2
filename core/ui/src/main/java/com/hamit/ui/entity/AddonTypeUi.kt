package com.hamit.ui.entity

import com.hamit.domain.entity.addon.AddonType
import com.hamit.ui.R

enum class AddonTypeUi(val titleStringRes: Int) {

    ALL(R.string.sort_type_enum_all),
    ADDON(R.string.sort_type_enum_addon),
    MAPS(R.string.sort_type_enum_maps),
    TEXTURE(R.string.sort_type_enum_texture),
    SKINS(R.string.sort_type_enum_skins);

    fun toAddonType() = when(this){
        AddonTypeUi.ALL -> null
        AddonTypeUi.ADDON -> AddonType.ADDON
        AddonTypeUi.MAPS -> AddonType.WORLD
        AddonTypeUi.TEXTURE -> AddonType.TEXTURE_PACK
        AddonTypeUi.SKINS -> AddonType.SKIN_PACK
    }
    companion object{
        fun fromAddonType(type: AddonType) = when(type){
            AddonType.WORLD -> AddonTypeUi.ADDON
            AddonType.ADDON -> AddonTypeUi.MAPS
            AddonType.TEXTURE_PACK -> AddonTypeUi.TEXTURE
            AddonType.SKIN_PACK -> AddonTypeUi.SKINS
        }
    }

}