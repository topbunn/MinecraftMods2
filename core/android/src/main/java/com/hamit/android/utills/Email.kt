package com.hamit.android.utills

fun String.isEmail() = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$").matches(this)