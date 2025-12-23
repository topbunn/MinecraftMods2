package com.hamit.instruction

import kotlinx.serialization.Serializable
import com.hamit.ui.R

@Serializable
data class InstructionEntity(
    val titleRes: Int,
    val imageRes: Int,
){

    companion object{

        fun getAddonInstruction() = listOf(
            InstructionEntity(R.string.instr_addon_1, R.drawable.manual_addon_1),
            InstructionEntity(R.string.instr_addon_2, R.drawable.manual_addon_2),
            InstructionEntity(R.string.instr_addon_3, R.drawable.manual_addon_3),
            InstructionEntity(R.string.instr_addon_4, R.drawable.manual_addon_4),
        )

        fun getWorldInstruction() = listOf(
            InstructionEntity(R.string.instr_world_1, R.drawable.manual_world_1),
            InstructionEntity(R.string.instr_world_2, R.drawable.manual_world_2),
            InstructionEntity(R.string.instr_world_3, R.drawable.manual_world_3),
            InstructionEntity(R.string.instr_addon_4, R.drawable.manual_addon_4),
        )


    }

}