package com.example.tripletriad

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.example.tripletriad.databinding.ActivityPlayBinding
import com.example.tripletriad.data.DataSource
import com.example.tripletriad.model.ListCard
import java.util.*

class PlayActivity : AppCompatActivity() {
    private val myList = DataSource.cards //ListCard
    private val enemyList: Array<Card> = Array(5){Card()} //Card
    // creates a 3x3 board filled with blank, grey cards
    var board: Array<Array<Card>> = Array(3){ Array(3){ Card() } }
    private lateinit var binding: ActivityPlayBinding
    private var draggingCard: Drawable?= null
    private var drawInt: Int = -1
    private var currentCard: Card ?= null

//    private var playerCardOne: ListCard?= null
//    private var playerCardTwo: ListCard?= null
//    private var playerCardThree: ListCard?= null
//    private var playerCardFour: ListCard?= null
//    private var playerCardFive: ListCard?= null
//
//    private var opponentCardOne: ListCard?= null
//    private var opponentCardTwo: ListCard?= null
//    private var opponentCardThree: ListCard?= null
//    private var opponentCardFour: ListCard?= null
//    private var opponentCardFive: ListCard?= null

    private var playerCardOne: ImageView?= null
    private var playerCardTwo: ImageView?= null
    private var playerCardThree: ImageView?= null
    private var playerCardFour: ImageView?= null
    private var playerCardFive: ImageView?= null

    private var opponentCardOne: ImageView?= null
    private var opponentCardTwo: ImageView?= null
    private var opponentCardThree: ImageView?= null
    private var opponentCardFour: ImageView?= null
    private var opponentCardFive: ImageView?= null

    private var topLeftSpace: ImageView?= null
    private var leftSpace: ImageView?= null
    private var botLeftSpace: ImageView?= null
    private var topMidSpace: ImageView?= null
    private var midSpace: ImageView?= null
    private var botMidSpace: ImageView?= null
    private var topRightSpace: ImageView?= null
    private var rightSpace: ImageView?= null
    private var botRightSpace: ImageView?= null



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_play)


        var playerListCardOne = myList[0]
        var playerListCardTwo = myList[1]
        var playerListCardThree = myList[2]
        var playerListCardFour = myList[3]
        var playerListCardFive = myList[10]

        // link cards to xml imageViews
        playerCardOne = findViewById(R.id.player_card_one)
        playerCardTwo = findViewById(R.id.player_card_two)
        playerCardThree = findViewById(R.id.player_card_three)
        playerCardFour = findViewById(R.id.player_card_four)
        playerCardFive = findViewById(R.id.player_card_five)

        // xml cards are set to the corresponding images
        playerCardOne?.setImageResource(playerListCardOne.imageResourceId)
        playerCardTwo?.setImageResource(playerListCardTwo.imageResourceId)
        playerCardThree?.setImageResource(playerListCardThree.imageResourceId)
        playerCardFour?.setImageResource(playerListCardFour.imageResourceId)
        playerCardFive?.setImageResource(playerListCardFive.imageResourceId)

        makeDragger(playerCardOne, playerListCardOne.imageResourceId)
        makeDragger(playerCardTwo, playerListCardTwo.imageResourceId)
        makeDragger(playerCardThree, playerListCardThree.imageResourceId)
        makeDragger(playerCardFour, playerListCardFour.imageResourceId)
        makeDragger(playerCardFive, playerListCardFive.imageResourceId)

        opponentCardOne = findViewById(R.id.opponent_card_one)
        opponentCardTwo = findViewById(R.id.opponent_card_two)
        opponentCardThree = findViewById(R.id.opponent_card_three)
        opponentCardFour = findViewById(R.id.opponent_card_four)
        opponentCardFive = findViewById(R.id.opponent_card_five)

        fillEnemyList()
        var opponentListCardOne = enemyList[0]
        var opponentListCardTwo = enemyList[1]
        var opponentListCardThree = enemyList[2]
        var opponentListCardFour = enemyList[3]
        var opponentListCardFive = enemyList[4]

        // enable if all-open rule is true, keep true for testing
        opponentCardOne?.setImageResource(opponentListCardOne.imageId)
        opponentCardTwo?.setImageResource(opponentListCardTwo.imageId)
        opponentCardThree?.setImageResource(opponentListCardThree.imageId)
        opponentCardFour?.setImageResource(opponentListCardFour.imageId)
        opponentCardFive?.setImageResource(opponentListCardFive.imageId)



        // bad, don't keep this drawable impl
//        makeDragger(playerCardOne, R.drawable.card1)
//        makeDragger(playerCardTwo, R.drawable.card2)
//        makeDragger(playerCardThree, R.drawable.card3)
//        makeDragger(playerCardFour, R.drawable.card4)
//        makeDragger(playerCardFive, R.drawable.card5)



//        opponentCardOne!!.imageResourceId = R.drawable.cardback
//        opponentCardTwo!!.imageResourceId = R.drawable.cardback
//        opponentCardThree!!.imageResourceId = R.drawable.cardback
//        opponentCardFour!!.imageResourceId = R.drawable.cardback
//        opponentCardFive!!.imageResourceId = R.drawable.cardback



        topLeftSpace = findViewById(R.id.top_left_space)
        leftSpace = findViewById(R.id.left_space)
        botLeftSpace = findViewById(R.id.bot_left_space)

        topLeftSpace?.setOnDragListener(dragListener)
        leftSpace?.setOnDragListener(dragListener)
        botLeftSpace?.setOnDragListener(dragListener)


        topMidSpace = findViewById(R.id.top_mid_space)
        midSpace = findViewById(R.id.mid_space)
        botMidSpace = findViewById(R.id.bot_mid_space)

        topMidSpace?.setOnDragListener(dragListener)
        midSpace?.setOnDragListener(dragListener)
        botMidSpace?.setOnDragListener(dragListener)


        topRightSpace = findViewById(R.id.top_right_space)
        rightSpace = findViewById(R.id.right_space)
        botRightSpace = findViewById(R.id.bot_right_space)

        topRightSpace?.setOnDragListener(dragListener)
        rightSpace?.setOnDragListener(dragListener)
        botRightSpace?.setOnDragListener(dragListener)

        //play game
    }

    fun playGame(){

    }


    // board spaces are drop spaces
    val dragListener = View.OnDragListener{ view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()

                view.invalidate()

                val v = event.localState as View
                val owner = v.parent as ViewGroup
                //owner.removeView(v)
                val destination = view as ImageView
                if(Objects.equals(view.drawable.getConstantState(), this.getResources().getDrawable(R.drawable.blank_square).getConstantState())){
                    destination.setImageResource(drawInt)
                    v.visibility = View.INVISIBLE
                }

               // destination.setBackgroundColor(getResources().getColor(R.color.teal_200))
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false
        }

    }


    fun spaceToCord(view: ImageView?= null): Array<Int> {
        var thing = ""
        when(view){
            topLeftSpace ->{
                return arrayOf(0, 0)
            }
            leftSpace ->{
                return arrayOf(1, 0)
            }
            botLeftSpace ->{
                return arrayOf(2, 0)
            }

            topMidSpace ->{
                return arrayOf(0, 1)
            }
            midSpace ->{
                return arrayOf(1, 1)
            }
            botMidSpace ->{
                return arrayOf(2, 1)
            }

            topRightSpace ->{
                return arrayOf(2, 2)
            }
            rightSpace ->{
                return arrayOf(2, 2)
            }
            botRightSpace ->{
                return arrayOf(2, 2)
            }
        }
        Toast.makeText(this, thing, Toast.LENGTH_SHORT).show()
        // something went wrong
        return arrayOf(-1, -1)
    }

    // make player cards draggable
    fun makeDragger(view: ImageView?= null, id: Int){
        view?.setOnLongClickListener{
            // move code to drag listener
            draggingCard = view.getDrawable()
            drawInt = id
            Toast.makeText(this, "Picked Up " + id + "", Toast.LENGTH_SHORT).show()
            val clipImage = draggingCard
            val clipText = "This is clipdata text"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            //view.visibility = View.INVISIBLE
            false
        }
    }

//    override fun onTouch(view: ImageView, motionEvent: MotionEvent) : Boolean{
//        var dX = 0.0f
//        var dY = 0.0f
//        when(motionEvent.getAction()){
//            MotionEvent.ACTION_DOWN -> {
//                dX = view.getX() - motionEvent.getRawX()
//                dY = view.getY() - motionEvent.getRawY()
//            }
//            MotionEvent.ACTION_MOVE -> {
//
//            }
//            MotionEvent.ACTION_UP -> {
//
//            }
//            else -> false
//        }
//        true
//    }

    fun playAI(){

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

    fun fillEnemyList(){
        var alreadyUsed = mutableSetOf<Int>()
        while(alreadyUsed.size < 5){
            val randomListCardIndex = (0..myList.size - 1).random()
            alreadyUsed.add(randomListCardIndex)
        }
        for(i in 0..4){
            val randomListCard = myList[alreadyUsed.elementAt(i)]
            val randomCard = Card(randomListCard.imageResourceId, randomListCard.name, randomListCard.north, randomListCard.east,
                                randomListCard.south, randomListCard.west)
            enemyList[i] =  randomCard
        }
    }
}