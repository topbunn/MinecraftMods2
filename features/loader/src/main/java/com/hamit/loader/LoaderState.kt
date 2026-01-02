package com.hamit.loader

data class LoaderState(
    val shouldGoApp: Boolean = false,
    val toNavigate: Boolean = false,
    val adInit: Boolean = false
)
