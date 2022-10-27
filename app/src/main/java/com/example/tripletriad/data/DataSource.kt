package com.example.tripletriad.data

import com.example.tripletriad.R
import com.example.tripletriad.model.ListCard


/**
 * An object to generate a static list of Triple Triad cards
 */
object DataSource {
    val cards: List<ListCard> = listOf(
        ListCard (
            //dummy values
            R.drawable.blank_square,
            "test1"
        ),
        ListCard (
            //dummy values
        R.drawable.blank_square,
        "test2"
        ),

        ListCard (
            R.drawable.blank_square,
            "test3"
        ),
        ListCard (
            R.drawable.blank_square,
            "test4"
        ),
        ListCard (
            R.drawable.blank_square,
            "test5"
        ),
        ListCard (
            R.drawable.blank_square,
            "test6"
        ),
        ListCard (
            R.drawable.blank_square,
            "test7"
        )
    )
}