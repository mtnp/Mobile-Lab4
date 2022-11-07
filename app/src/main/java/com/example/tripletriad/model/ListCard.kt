package com.example.tripletriad.model

import androidx.annotation.DrawableRes
import com.example.tripletriad.R

data class ListCard (
    @DrawableRes var imageResourceId: Int,
    val name: String,
    val north: Int,
    val east: Int,
    val south: Int,
    val west: Int
)