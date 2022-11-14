package com.nhatsilas.tripletriad.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_table")
data class ListCard (
    @PrimaryKey(autoGenerate = true)
    @DrawableRes var imageResourceId: Int,
    val name: String,
    val north: Int,
    val east: Int,
    val south: Int,
    val west: Int
)