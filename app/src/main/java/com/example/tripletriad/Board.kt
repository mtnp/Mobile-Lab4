package com.example.tripletriad

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tripletriad.databinding.ActivityPlayBinding

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

        playerCardTwo = findViewById(R.id.player_card_two)
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