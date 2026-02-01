package com.l13devstudio.guide

import android.content.Context
import com.l13devstudio.ui.R
import kotlinx.serialization.Serializable

@Serializable
data class GuideEntity(
    val text: String,
    val imageResId: Int,
){

    companion object{

        fun getAddonGuide(context: Context): List<GuideEntity> {
            val guideTextArray = context.resources.getStringArray(R.array.guide_addon_array)
            return listOf(
                GuideEntity(guideTextArray[0], R.drawable.guide_addon_1),
                GuideEntity(guideTextArray[1], R.drawable.guide_addon_2),
                GuideEntity(guideTextArray[2], R.drawable.guide_addon_3),
                GuideEntity(guideTextArray[3], R.drawable.guide_addon_4),
                GuideEntity(guideTextArray[4], R.drawable.guide_addon_5),
                GuideEntity(guideTextArray[5], R.drawable.guide_addon_6),
                GuideEntity(guideTextArray[6], R.drawable.guide_addon_7),
            )
        }

        fun getMapGuide(context: Context): List<GuideEntity> {
            val guideTextArray = context.resources.getStringArray(R.array.guide_map_array)
            return listOf(
                GuideEntity(guideTextArray[0], R.drawable.guide_map_1),
                GuideEntity(guideTextArray[1], R.drawable.guide_map_2),
                GuideEntity(guideTextArray[2], R.drawable.guide_map_3),
                GuideEntity(guideTextArray[3], R.drawable.guide_map_4),
                GuideEntity(guideTextArray[4], R.drawable.guide_map_5),
                GuideEntity(guideTextArray[5], R.drawable.guide_map_6),
                GuideEntity(guideTextArray[6], R.drawable.guide_map_7),
            )
        }


    }

}