package com.example.tripletriad.model

import androidx.annotation.DrawableRes

data class ListCard (
    @DrawableRes val imageResourceId: Int,
    val name: String
)