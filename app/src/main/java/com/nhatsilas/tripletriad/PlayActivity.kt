package com.nhatsilas.tripletriad

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nhatsilas.tripletriad.databinding.ActivityPlayBinding
import com.nhatsilas.tripletriad.data.DataSource
import com.nhatsilas.tripletriad.model.ListCard
import com.nhatsilas.tripletriad.model.PlayActivityViewModel
import java.util.*

class PlayActivity : AppCompatActivity() {
    private val allCardsList = DataSource.cards //ListCard
    private val enemyList: Array<Card> = Array(5){Card()} //Card
    private val myList: Array<Card> = Array(5){Card()} //Card
    // creates a 3x3 board filled with blank, grey cards
    var board: Array<Array<Card>> = Array(3){ Array(3){ Card() } }
    private lateinit var binding: ActivityPlayBinding
    private var draggingCard: Drawable?= null
    private var drawInt: Int = -1

    // black is error color; turn colors are blue/red; for now teal/purple
    private var currentColor: Int = R.color.black
    private var cardsPlaced: Int = 0

    lateinit var viewModel: PlayActivityViewModel
    private var savedEnemy: IntArray = IntArray(30)
    private var savedPlayer: IntArray = IntArray(30)
    private var savedBoard: IntArray = IntArray(54)

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

    private var musicPlaying: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_play)

        // for screen rotation data persistence
        viewModel = ViewModelProvider(this).get(PlayActivityViewModel::class.java)

        // play music
        MusicPlayer.playSound(this)

        // set up music toggle
        var muteBtn: ImageView = findViewById(R.id.muteBtn)
        muteBtn.setImageResource(R.drawable.volume)
        muteBtn.setOnClickListener {
            if (MusicPlayer.isPlaying()) {
                MusicPlayer.mpStop()
                musicPlaying = false
                muteBtn.setImageResource(R.drawable.volumemute)

                Log.d("Music", "Is muted")
            } else if (!MusicPlayer.isPlaying()) {
                MusicPlayer.playSound(this)
                musicPlaying = true
                muteBtn.setImageResource(R.drawable.volume)
                Log.d("Music", "Is playing")
            }
        }


        if(savedInstanceState != null){
            // there was data from the board, restore it
            savedPlayer = savedInstanceState.getIntArray("SavedPlayer")!!
            savedEnemy = savedInstanceState.getIntArray("SavedEnemy")!!
            savedBoard = savedInstanceState.getIntArray("SavedBoard")!!


            // restore previous player hand
            IntArraySettingCardArray(savedPlayer, myList)
            Log.d("PlayerHand", ""+cardIsEmpty(myList[0])+""+cardIsEmpty(myList[1])
                    +""+cardIsEmpty(myList[2])+""+cardIsEmpty(myList[3])+""+cardIsEmpty(myList[4]))

            // restore previous enemy hand
            IntArraySettingCardArray(savedEnemy, enemyList)
            Log.d("EnemyHand", ""+cardIsEmpty(enemyList[0])+""+cardIsEmpty(enemyList[1])
                    +""+cardIsEmpty(enemyList[2])+""+cardIsEmpty(enemyList[3])+""+cardIsEmpty(enemyList[4]))

            //restore board
            IntArraySettingBoardArray(savedBoard)
        }
        else {
            // randomly give cards if no saved data
            fillList(myList)
            fillList(enemyList)
        }

        // fills both hands with cards
        var playerListCardOne = myList[0]
        var playerListCardTwo = myList[1]
        var playerListCardThree = myList[2]
        var playerListCardFour = myList[3]
        var playerListCardFive = myList[4]

        var opponentListCardOne = enemyList[0]
        var opponentListCardTwo = enemyList[1]
        var opponentListCardThree = enemyList[2]
        var opponentListCardFour = enemyList[3]
        var opponentListCardFive = enemyList[4]


        // link cards to xml imageViews
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


        // cards in both hands are set to the corresponding images
        playerCardOne?.setImageResource(playerListCardOne.imageId)
        playerCardTwo?.setImageResource(playerListCardTwo.imageId)
        playerCardThree?.setImageResource(playerListCardThree.imageId)
        playerCardFour?.setImageResource(playerListCardFour.imageId)
        playerCardFive?.setImageResource(playerListCardFive.imageId)

        opponentCardOne?.setImageResource(opponentListCardOne.imageId)
        opponentCardTwo?.setImageResource(opponentListCardTwo.imageId)
        opponentCardThree?.setImageResource(opponentListCardThree.imageId)
        opponentCardFour?.setImageResource(opponentListCardFour.imageId)
        opponentCardFive?.setImageResource(opponentListCardFive.imageId)


        // make player cards draggable
        makeDragger(playerCardOne, playerListCardOne)
        makeDragger(playerCardTwo, playerListCardTwo)
        makeDragger(playerCardThree, playerListCardThree)
        makeDragger(playerCardFour, playerListCardFour)
        makeDragger(playerCardFive, playerListCardFive)


        // links board spaces and makes them into droppable locations
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

    }

    override fun onSaveInstanceState(outState: Bundle) {
        savedEnemy = cardArrayToIntArray(enemyList)
        savedPlayer = cardArrayToIntArray(myList)
        savedBoard = boardStateToIntArray()

        outState.putIntArray("SavedEnemy", savedEnemy)
        outState.putIntArray("SavedPlayer", savedPlayer)
        outState.putIntArray("SavedBoard", savedBoard)
        super.onSaveInstanceState(outState)
    }

    // pause music when leaving the activity
    override fun onPause() {
        MusicPlayer.mpStop()
        super.onPause()
    }

    // play music when returning to activity
    override fun onRestart() {
        MusicPlayer.playSound(this)
        super.onRestart()
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
                if(checkBoardFull()){
                    false
                }
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                val draggedNumbers = dragData.toString().split(",").map { it.toInt() }.toIntArray()
                val draggedCard = arrayToCard(draggedNumbers)
                draggedCard.color = 0

                view.invalidate()

                val v = event.localState as ImageView
                val owner = v.parent as ViewGroup
                //owner.removeView(v)
                val destination = view as ImageView
                if(Objects.equals(view.drawable.constantState, this.resources.getDrawable(R.drawable.emptycard).constantState)){
                    // this space is available, can place card here
                    var cordArray = spaceToCord(destination)
                    var row = cordArray[0]
                    var col = cordArray[1]

                    // game starts at 0 cards placed and is player's turn
                    if(cardsPlaced % 2 == 0){
                        currentColor = R.color.blue
                    }
                    else{
                        currentColor = R.color.red
                    }

                    placeCard(draggedCard, row, col)
                    Log.d("D", "North:" + draggedNumbers[0] + " East:" + draggedNumbers[1] + " South:" + draggedNumbers[2] + " West:" + draggedNumbers[3])
                    Log.d("D", "Player placed card at square: " + row +", " + col)
                    destination.setImageResource(drawInt)
                    destination.setBackgroundColor(resources.getColor(currentColor))
                    //v.visibility = View.INVISIBLE
                    v.setImageResource(R.drawable.emptyslot)
                    v.setEnabled(false)
                    removeFromPlayerHand(v)
                    playAI()
                }
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false
        }

    }


    // returns an array with 2 ints for row and col based on which
    // square the card was placed
    fun spaceToCord(view: ImageView?= null): IntArray {
        when(view){
            topLeftSpace ->{
                return intArrayOf(0, 0)
            }
            leftSpace ->{
                return intArrayOf(1, 0)
            }
            botLeftSpace ->{
                return intArrayOf(2, 0)
            }

            topMidSpace ->{
                return intArrayOf(0, 1)
            }
            midSpace ->{
                return intArrayOf(1, 1)
            }
            botMidSpace ->{
                return intArrayOf(2, 1)
            }

            topRightSpace ->{
                return intArrayOf(0, 2)
            }
            rightSpace ->{
                return intArrayOf(1, 2)
            }
            botRightSpace ->{
                return intArrayOf(2, 2)
            }
        }
        // Toast.makeText(this, thing, Toast.LENGTH_SHORT).show()
        // something went wrong
        return intArrayOf(-1, -1)
    }

    // make player cards draggable
    fun makeDragger(view: ImageView?= null, card: Card){
        view?.setOnLongClickListener{
            // move code to drag listener
            // TODO: figure out how to send multiple clipText thru clipData
            draggingCard = view.drawable
            drawInt = card.imageId
            val clipText = "" + card.northVal + "," + card.eastVal + "," + card.southVal + "," + card.westVal
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            //view.visibility = View.INVISIBLE
            true
        }
    }

    fun playAI(){
        if(checkBoardFull()){
            return
        }
        var cardIndex = randomHand()
        val card = enemyList[cardIndex]
        val cardView = indexToCardViewAI(cardIndex)

        card.color = 1
        var cords = AIOptimal(card)
        var row = cords[0]
        var col = cords[1]
        if (row == -1 && col == -1) {
            cords = AIRandom()
            row = cords[0]
            col = cords[1]
        }
        Log.d("D", "AI Row:" + row + " AI Col:" + col)

        val view = cordToSpace(row, col)
        Log.d("D", "AI Space:" + view?.id)
        Log.d("D", "AI Turn...Cards Placed:" + cardsPlaced)

        if(cardsPlaced % 2 == 0){
            currentColor = R.color.blue
        }
        else{
            currentColor = R.color.red
        }

        placeCard(card, row, col)
        cardView?.setImageResource(R.drawable.emptyslot)
        view?.setImageResource(card.imageId)
        view?.setBackgroundColor(resources.getColor(currentColor))
        board[row][col].color = 1
        setCardToEmpty(enemyList[cardIndex])
    }

    fun linearHand(): Int{
        return cardsPlaced / 2
    }

    fun randomHand(): Int{
        var result = (0..4).random()
        var card = indexToCardViewAI(result)

        while(card?.drawable?.constantState?.equals
                (resources.getDrawable(R.drawable.emptyslot).constantState)!!){
            result = (0..4).random()
            card = indexToCardViewAI(result)

        }

        return result
    }

    fun optimalHand(): Int{
        return -1
    }

    fun indexToCardViewAI(index: Int): ImageView? {
        when(index){
            0 -> {
                return opponentCardOne
            }
            1 -> {
                return opponentCardTwo
            }
            2 -> {
                return opponentCardThree
            }
            3 -> {
                return opponentCardFour
            }
            4 -> {
                return opponentCardFive
            }
        }

        return opponentCardOne
    }

    fun cordToSpace(row: Int, col: Int): ImageView? {
        when {
            row == 0 && col == 0 -> {
                return topLeftSpace
            }
            row == 1 && col == 0 -> {
                return leftSpace
            }
            row == 2 && col == 0 -> {
                return botLeftSpace
            }

            row == 0 && col == 1 -> {
                return topMidSpace
            }
            row == 1 && col == 1 -> {
                return midSpace
            }
            row == 2 && col == 1 -> {
                return botMidSpace
            }

            row == 0 && col == 2 -> {
                return topRightSpace
            }
            row == 1 && col == 2 -> {
                return rightSpace
            }
            row == 2 && col == 2 -> {
                return botRightSpace
            }
        }
        return midSpace
    }

    // updates the backend board
    fun placeCard(card: Card, row: Int, col: Int){
        if(!spotIsEmpty(row, col)){
            // spot is already taken

        }
        else{ // flip other cards if they exist and are opponent's cards
            board[row][col] = card
            interactOtherCards(row, col, card)
            cardsPlaced++
        }
    }

    // return true if board is full
    fun checkBoardFull() : Boolean{
        var playerPoints = 0
        var AIPoints = 1
        var playerScore = 5
        var AIScore = 5
        var isFull = false
        for(r in 0..2){
            for(c in 0..2){
                var color = board[r][c].color
                if(color == -1) {
                    // found a space that hasn't been used yet
                    isFull = true
                }
                else if(color == 0){
                    playerScore++
                    AIScore--

                    playerPoints++
                }
                else{
                    playerScore--
                    AIScore++

                    AIPoints++
                }
            }
        }

        Log.d("Score", "player:" + playerScore + " ai:" + AIScore)
        if(isFull){
            return false
        }

        Log.d("D", "Board is filled, game is over")
        if(playerPoints > AIPoints) {
            Log.d("D", "Player WIN")
            Toast.makeText(this, "Player WIN", Toast.LENGTH_LONG).show()
        }
        else if(playerPoints == AIPoints) {
            Log.d("D", "TIE")
            Toast.makeText(this, "Player TIE", Toast.LENGTH_LONG).show()
        }
        else {
            Log.d("D", "Player LOSE")
            Toast.makeText(this, "Player LOSE", Toast.LENGTH_LONG).show()
        }
        return true
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
            Log.d("D", "North Interaction")
            if(card.northVal > board[row - 1][col].southVal && isOpponentCard(row - 1, col, thisColor)){
                Log.d("D", "North flip")
                board[row - 1][col].color = thisColor
                val northView = cordToSpace(row - 1, col)
                northView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }

        // check east side
        if(isWithinBounds(row, col + 1)){
            Log.d("D", "east Interaction")
            if(card.eastVal > board[row][col + 1].westVal && isOpponentCard(row, col + 1, thisColor)){
                Log.d("D", "east flip")
                board[row][col + 1].color = thisColor
                val eastView = cordToSpace(row, col + 1)
                eastView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }

        // check south side
        if(isWithinBounds(row + 1, col)){
            Log.d("D", "south Interaction")
            if(card.southVal > board[row + 1][col].northVal  && isOpponentCard(row + 1, col, thisColor)){
                Log.d("D", "south flip")
                board[row + 1][col].color = thisColor
                val southView = cordToSpace(row + 1, col)
                southView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }

        // check west side
        if(isWithinBounds(row, col - 1)){
            Log.d("D", "west Interaction")
            if(card.westVal > board[row][col - 1].eastVal && isOpponentCard(row, col - 1, thisColor)){
                Log.d("D", "west flip")
                board[row][col - 1].color = thisColor
                val westView = cordToSpace(row, col - 1)
                westView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }
    }

    fun fillList(thisList : Array<Card>){
        var alreadyUsed = mutableSetOf<Int>()
        while(alreadyUsed.size < 5){
            val randomListCardIndex = (0..allCardsList.size - 1).random()
            alreadyUsed.add(randomListCardIndex)
        }
        for(i in 0..4){
            val randomListCard = allCardsList[alreadyUsed.elementAt(i)]
            val randomCard = Card(randomListCard.imageResourceId, randomListCard.name, randomListCard.north, randomListCard.east,
                randomListCard.south, randomListCard.west)
            thisList[i] =  randomCard
        }
    }

    fun listCardToCard(listCard: ListCard) : Card{
        return Card(listCard.imageResourceId, listCard.name,
            listCard.north, listCard.east, listCard.south, listCard.west)
    }

    fun arrayToCard(numbers: IntArray): Card{
        return Card(R.drawable.cardback, "DEFAULT", numbers[0], numbers[1], numbers[2], numbers[3])
    }

    fun AILinear(): IntArray{
        for(row in 0..2){
            for(col in 0..2){
                if(spotIsEmpty(row, col)){
                    Log.d("D", "AI Found row:" + row + " col:"+ col +" to be empty.")
                    return intArrayOf(row, col)
                }
            }
        }

        return intArrayOf(-1, -1)
    }

    fun AIRandom(): IntArray{
        var row = (0..2).random()
        var col = (0..2).random()
        while(!spotIsEmpty(row, col)){
            row = (0..2).random()
            col = (0..2).random()
        }

        Log.d("D", "AI Found row:" + row + " col:"+ col +" to be empty.")
        return intArrayOf(row, col)
    }

    fun isEnemy(myCard: Card, otherCard: Card): Boolean{
        return myCard.color != otherCard.color && otherCard.color != -1
    }

    fun AIOptimal(card : Card): IntArray{
        for (row in 0..2) {
            for (col in 0..2) {
                if (spotIsEmpty(row, col)) {
                    if(isWithinBounds(row - 1, col) && !spotIsEmpty(row - 1, col)){
                        var otherCard = board[row - 1][col]
                        Log.d("D", "North Interaction")
                        if(card.northVal > otherCard.southVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }

                    // check east side
                    if(isWithinBounds(row, col + 1) && !spotIsEmpty(row, col + 1)){
                        var otherCard = board[row][col + 1]
                        Log.d("D", "east Interaction")
                        if(card.eastVal > otherCard.westVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }

                    // check south side
                    if(isWithinBounds(row + 1, col) && !spotIsEmpty(row + 1, col)){
                        var otherCard = board[row + 1][col]
                        Log.d("D", "south Interaction")
                        if(card.southVal > otherCard.northVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }

                    // check west side
                    if(isWithinBounds(row, col - 1)  && !spotIsEmpty(row, col - 1)){
                        var otherCard = board[row][col - 1]
                        Log.d("D", "west Interaction")
                        if(card.westVal > otherCard.eastVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }
                }
            }
        }
        return intArrayOf(-1, -1)
    }

    fun spotIsEmpty(row: Int, col: Int): Boolean{
        return board[row][col].color == -1
    }

    fun cardArrayToIntArray(thisList: Array<Card>): IntArray{
        var result = IntArray(30)
        var resultIndex = 0

        for(i in 0..4){
            result[resultIndex++] = thisList[i].imageId
            result[resultIndex++] = thisList[i].color
            result[resultIndex++] = thisList[i].northVal
            result[resultIndex++] = thisList[i].eastVal
            result[resultIndex++] = thisList[i].southVal
            result[resultIndex++] = thisList[i].westVal
        }

        return result
    }

    // saves the data of the cards on the board to an array
    fun boardStateToIntArray(): IntArray{
        var result = IntArray(54)
        var resultIndex = 0
        for(row in 0..2){
            for(col in 0..2){
                var card = board[row][col]
                result[resultIndex] = card.imageId
                resultIndex++
                result[resultIndex] = card.color
                resultIndex++
                result[resultIndex] = card.northVal
                resultIndex++
                result[resultIndex] = card.eastVal
                resultIndex++
                result[resultIndex] = card.southVal
                resultIndex++
                result[resultIndex] = card.westVal
                resultIndex++
            }
        }


        return result
    }

    fun IntArraySettingCardArray(data: IntArray, target: Array<Card>){
        var dataIndex = 0
        for(i in 0..4){
            target[i].imageId = data[dataIndex]
            dataIndex++
            target[i].color = data[dataIndex]
            dataIndex++
            target[i].northVal = data[dataIndex]
            dataIndex++
            target[i].eastVal = data[dataIndex]
            dataIndex++
            target[i].southVal = data[dataIndex]
            dataIndex++
            target[i].westVal = data[dataIndex]
            dataIndex++
        }
    }

    fun IntArraySettingBoardArray(data: IntArray){
        var dataIndex = 0
        for(row in 0..2){
            for(col in 0..2){
                board[row][col].imageId = data[dataIndex]
                dataIndex++
                board[row][col].color = data[dataIndex]
                dataIndex++
                board[row][col].northVal = data[dataIndex]
                dataIndex++
                board[row][col].eastVal = data[dataIndex]
                dataIndex++
                board[row][col].southVal = data[dataIndex]
                dataIndex++
                board[row][col].westVal = data[dataIndex]
                dataIndex++
            }
        }
    }

    fun setCardToEmpty(card: Card){
        card.imageId = R.drawable.emptyslot
        //card.color = -1
        //card.northVal = -1
        //card.eastVal = -1
        //card.southVal = -1
        //card.westVal = -1
    }

    fun cardIsEmpty(card: Card): Boolean{
        return card.imageId == R.drawable.emptyslot
    }

    fun removeFromPlayerHand(card: ImageView){
        val thing = card?.drawable?.constantState
        when(thing){
            playerCardOne?.drawable?.constantState!! ->{
                setCardToEmpty(myList[0])
            }

            playerCardTwo?.drawable?.constantState!! ->{
                setCardToEmpty(myList[1])
            }

            playerCardThree?.drawable?.constantState!! ->{
                setCardToEmpty(myList[2])
            }

            playerCardFour?.drawable?.constantState!! ->{
                setCardToEmpty(myList[3])
            }

            playerCardFive?.drawable?.constantState!! ->{
                setCardToEmpty(myList[4])
            }
        }
    }
}