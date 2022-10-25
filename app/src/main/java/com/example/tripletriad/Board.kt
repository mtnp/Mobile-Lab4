package com.example.tripletriad

class Board {

    // creates a 3x3 board filled with blank, grey cards
    var board: Array<Array<Card>> = Array(3){ Array(3){ Card() } }


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