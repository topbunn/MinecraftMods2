package com.hamit.data.api.dto.mods

import com.google.gson.annotations.SerializedName

data class EntryListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("mods") val items: List<EntryDto>
)
