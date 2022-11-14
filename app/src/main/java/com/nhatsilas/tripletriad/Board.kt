package com.nhatsilas.tripletriad

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nhatsilas.tripletriad.databinding.ActivityPlayBinding

class Board : AppCompatActivity() {

    // creates a 3x3 board filled with blank, grey cards
    var board: Array<Array<Card>> = Array(3){ Array(3){ Card() } }
    private lateinit var binding: ActivityPlayBinding
    private var draggingCard: ImageView ?= null
    private var currentCard: Card ?= null

    private var playerCardOne: ImageView ?= null
    private var playerCardTwo: ImageView ?= null
    private var playerCardThree: ImageView ?= null
    private var playerCardFour: ImageView ?= null
    private var playerCardFive: ImageView ?= null

    private var opponentCardOne: ImageView ?= null
    private var opponentCardTwo: ImageView ?= null
    private var opponentCardThree: ImageView ?= null
    private var opponentCardFour: ImageView ?= null
    private var opponentCardFive: ImageView ?= null

    private var topLeftSpace: ImageView ?= null
    private var LeftSpace: ImageView ?= null
    private var botLeftSpace: ImageView ?= null
    private var topMidSpace: ImageView ?= null
    private var midSpace: ImageView ?= null
    private var botMidSpace: ImageView ?= null
    private var topRightSpace: ImageView ?= null
    private var RightSpace: ImageView ?= null
    private var botRightSpace: ImageView ?= null



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_play)

        playerCardOne = findViewById(R.id.player_card_one)
        playerCardOne?.setImageResource(R.drawable.cardback)
        playerCardOne?.setOnClickListener{it
            Toast.makeText(this, "Hello?", Toast.LENGTH_SHORT).show()
            val clipText = "clipData text"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }




        playerCardTwo = findViewById(R.id.player_card_two)
        playerCardTwo!!.setImageResource(R.drawable.cardback)
        playerCardThree = findViewById(R.id.player_card_three)
        playerCardFour = findViewById(R.id.player_card_four)
        playerCardFive = findViewById(R.id.player_card_five)

        opponentCardOne = findViewById(R.id.opponent_card_one)
        opponentCardTwo = findViewById(R.id.opponent_card_two)
        opponentCardThree = findViewById(R.id.opponent_card_three)
        opponentCardFour = findViewById(R.id.opponent_card_four)
        opponentCardFive = findViewById(R.id.opponent_card_five)

        topLeftSpace = findViewById(R.id.top_left_space)
        LeftSpace = findViewById(R.id.left_space)
        botLeftSpace = findViewById(R.id.bot_left_space)

        topMidSpace = findViewById(R.id.top_mid_space)
        midSpace = findViewById(R.id.mid_space)
        botMidSpace = findViewById(R.id.bot_mid_space)

        topRightSpace = findViewById(R.id.top_right_space)
        RightSpace = findViewById(R.id.right_space)
        botRightSpace = findViewById(R.id.bot_right_space)


    }

    val dragListener = topLeftSpace?.setOnDragListener{ v, e ->
        // Handles each of the expected events.
        when (e.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                // Determines if this View can accept the dragged data.
                if (e.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // As an example of what your application might do, applies a blue color tint
                    // to the View to indicate that it can accept data.
                    (v as? ImageView)?.setColorFilter(Color.BLUE)

                    // Invalidate the view to force a redraw in the new tint.
                    v.invalidate()

                    // Returns true to indicate that the View can accept the dragged data.
                    true
                } else {
                    // Returns false to indicate that, during the current drag and drop operation,
                    // this View will not receive events again until ACTION_DRAG_ENDED is sent.
                    false
                }
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                // Applies a green tint to the View.
                (v as? ImageView)?.setColorFilter(Color.GREEN)

                // Invalidates the view to force a redraw in the new tint.
                v.invalidate()

                // Returns true; the value is ignored.
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
                // Ignore the event.
                true
            DragEvent.ACTION_DRAG_EXITED -> {
                // Resets the color tint to blue.
                (v as? ImageView)?.setColorFilter(Color.BLUE)

                // Invalidates the view to force a redraw in the new tint.
                v.invalidate()

                // Returns true; the value is ignored.
                true
            }
            DragEvent.ACTION_DROP -> {
                // Gets the item containing the dragged data.
                val item: ClipData.Item = e.clipData.getItemAt(0)

                // Gets the text data from the item.
                val dragData = item.text

                // Displays a message containing the dragged data.
                Toast.makeText(this, "Dragged data is $dragData", Toast.LENGTH_LONG).show()

                // Turns off any color tints.
                (v as? ImageView)?.clearColorFilter()

                // Invalidates the view to force a redraw.
                v.invalidate()

                // Returns true. DragEvent.getResult() will return true.
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                // Turns off any color tinting.
                (v as? ImageView)?.clearColorFilter()

                // Invalidates the view to force a redraw.
                v.invalidate()

                // Does a getResult(), and displays what happened.
                when(e.result) {
                    true ->
                        Toast.makeText(this, "The drop was handled.", Toast.LENGTH_LONG)
                    else ->
                        Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_LONG)
                }.show()

                // Returns true; the value is ignored.
                true
            }
            else -> {
                // An unknown action type was received.
                Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.")
                false
            }
        }
    }



    fun placeCard(card: Card, row: Int, col: Int){
        if(board[row][col].color != -1){ // spot is already taken
            // do nothing or display message that card can't be placed here
        }
        else{ // flip other cards if they exist and are opponent's cards
            interactOtherCards(row, col, card)
        }
    }


    fun isWithinBounds(row: Int, col: Int): Boolean {
        return row != 3 && row != -1 && col != 3 && col != -1
    }

    fun isOpponentCard(row: Int, col: Int, playerColor: Int): Boolean {
        return board[row][col].color != playerColor && board[row][col].color != -1
    }

    fun interactOtherCards(row: Int, col: Int, card: Card){
        board[row][col] = card
        val thisColor = card.color

        // check north side
        if(isWithinBounds(row - 1, col)){
            if(card.northVal > board[row - 1][col].southVal && isOpponentCard(row - 1, col, thisColor)){
                board[row - 1][col].color = thisColor
            }
        }

        // check east side
        if(isWithinBounds(row, col + 1)){
            if(card.eastVal > board[row][col + 1].westVal && isOpponentCard(row, col + 1, thisColor)){
                board[row][col + 1].color = thisColor
            }
        }

        // check south side
        if(isWithinBounds(row + 1, col)){
            if(card.southVal > board[row + 1][col].northVal  && isOpponentCard(row + 1, col, thisColor)){
                board[row + 1][col].color = thisColor
            }
        }

        // check west side
        if(isWithinBounds(row, col - 1)){
            if(card.westVal > board[row][col - 1].eastVal && isOpponentCard(row, col - 1, thisColor)){
                board[row][col - 1].color = thisColor
            }
        }
    }

}