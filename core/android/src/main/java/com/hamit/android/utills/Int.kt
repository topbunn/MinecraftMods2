package com.hamit.android.utills

fun Long.bytesToMb(): Float{
    val result = (this.toFloat() / 1024 / 1024)
    return String.format("%.2f", result).toFloat()
}