package com.example.tripletriad.model

import androidx.annotation.DrawableRes

data class ListCard (
    @DrawableRes val imageResourceId: Int,
    val name: String,
    val north: Int,
    val east: Int,
    val south: Int,
    val west: Int
)