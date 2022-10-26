package com.example.tripletriad.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import com.example.tripletriad.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripletriad.data.DataSource

/**
 * Adapter to inflate the appropriate list item layout and populate the view with information
 * from the appropriate data source
 */
class CardAdapter (
    private val context: Context?,
    private val layout: Int
): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
}

// Initializes local data using the List found in data/DataSource
// val myList = DataSource.dogs
private val myList = DataSource.cards
/**
 * Initialize view elements
 */
class CardViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
    // Declares and initialize all of the list item UI components
    // This includes the picture of the cat, their name, their sex,
    // and their age.

    val cardImage : ImageView = view!!.findViewById(R.id.cardImage)
    val cardName : TextView = view!!.findViewById(R.id.cardName)

}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
    // we set the newLayout variable based on the layout we're on, this is passed
    // to the holder
    // if layout == 3, then we inflate the grid layout
    // if layout == 1 or 2, then we inflate the horizontal and vertical layout
    val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_list, parent, false)

    // we pass the new layout into the view holder
    return CardViewHolder(view)
}

// this is for cats
override fun getItemCount(): Int = myList.size

override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
    val resources = context?.resources

    // We get the information for the current cat, set the image resource
    // to that cat's picture, set the text for its name, set the text for
    // its sex, and sets the text for its age
    val card = myList[position]
    holder.cardImage.setImageResource(card.imageResourceId)
    holder.cardName.text = card.name
}
}