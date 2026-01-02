package com.hamit.guide

import kotlinx.serialization.Serializable
import com.hamit.ui.R

@Serializable
data class GuideEntity(
    val titleResourceId: Int,
    val previewResourceId: Int,
){

    companion object{

        fun getAddonGuide() = listOf(
            GuideEntity(R.string.instr_addon_1, R.drawable.manual_addon_1),
            GuideEntity(R.string.instr_addon_2, R.drawable.manual_addon_2),
            GuideEntity(R.string.instr_addon_3, R.drawable.manual_addon_3),
            GuideEntity(R.string.instr_addon_4, R.drawable.manual_addon_4),
        )

        fun getWorldGuide() = listOf(
            GuideEntity(R.string.instr_world_1, R.drawable.manual_world_1),
            GuideEntity(R.string.instr_world_2, R.drawable.manual_world_2),
            GuideEntity(R.string.instr_world_3, R.drawable.manual_world_3),
            GuideEntity(R.string.instr_addon_4, R.drawable.manual_addon_4),
        )


    }

}