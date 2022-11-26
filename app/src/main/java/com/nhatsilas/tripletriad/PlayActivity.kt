package com.nhatsilas.tripletriad

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.nhatsilas.tripletriad.databinding.ActivityPlayBinding
import com.nhatsilas.tripletriad.data.DataSource
import com.nhatsilas.tripletriad.model.ListCard
import com.nhatsilas.tripletriad.model.PlayActivityViewModel
import android.os.Handler
import android.os.Looper
import java.util.*

class PlayActivity : AppCompatActivity() {
    private val allCardsList = DataSource.cards //ListCard
    private val enemyList: Array<Card> = Array(5){Card()} //Card
    private val playerList: Array<Card> = Array(5){Card()} //Card
    // creates a 3x3 board filled with blank, grey cards
    var board: Array<Array<Card>> = Array(3){ Array(3){ Card() } }
    private lateinit var binding: ActivityPlayBinding

    // black is error color; turn colors are blue/red; for now teal/purple
    private var currentColor: Int = R.color.black
    private var playerColor: Int = R.color.blue
    private var enemyColor: Int = R.color.red
    private var cardsPlaced: Int = 0

    lateinit var viewModel: PlayActivityViewModel
    private var savedEnemy: IntArray = IntArray(30)
    private var savedPlayer: IntArray = IntArray(30)
    private var savedBoard: IntArray = IntArray(54)

    private var playerHandOne: ImageView?= null
    private var playerHandTwo: ImageView?= null
    private var playerHandThree: ImageView?= null
    private var playerHandFour: ImageView?= null
    private var playerHandFive: ImageView?= null

    private var enemyHandOne: ImageView?= null
    private var enemyHandTwo: ImageView?= null
    private var enemyHandThree: ImageView?= null
    private var enemyHandFour: ImageView?= null
    private var enemyHandFive: ImageView?= null

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
        if(savedInstanceState == null)
            MusicPlayer.playSound(this)

        // set up music toggle
        var muteBtn: ImageView = findViewById(R.id.muteBtn)

        if(savedInstanceState != null){
            // get last saved music state
            musicPlaying = savedInstanceState.getBoolean("SavedMusic")
            if (!musicPlaying) {
                // music was not playing
                MusicPlayer.mpStop()
                muteBtn.setImageResource(R.drawable.volumemutegray)
//                Log.d("Restoring Music", "to be off")

            } else if (musicPlaying) {
                // music was playing
                MusicPlayer.playSound(this)
                muteBtn.setImageResource(R.drawable.volumegray)
//                Log.d("Restoring Music", "to be on")
            }
        }

        muteBtn.setOnClickListener {
            if (musicPlaying) {
                // clicked when music was playing, so mute it
                MusicPlayer.mpStop()
                musicPlaying = false
                muteBtn.setImageResource(R.drawable.volumemutegray)
//                Log.d("Turning music", "Off")
            } else if (!musicPlaying) {
                // clicked when music was off, so play it
                MusicPlayer.playSound(this)
                musicPlaying = true
                muteBtn.setImageResource(R.drawable.volumegray)
//                Log.d("Turning music", "On")
            }
        }

        // set up back button
        var backBtn : ImageView = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        var resetBtn : ImageView = findViewById(R.id.resetBtn)
        resetBtn.setOnClickListener {
            startActivity(Intent(this, PlayActivity::class.java))
        }

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


        if(savedInstanceState != null){
            // there was data from the board, restore it
            savedPlayer = savedInstanceState.getIntArray("SavedPlayer")!!
            savedEnemy = savedInstanceState.getIntArray("SavedEnemy")!!
            savedBoard = savedInstanceState.getIntArray("SavedBoard")!!


            // restore previous player hand
            IntArraySettingCardArray(savedPlayer, playerList)
//            Log.d("PlayerHand", ""+cardIsEmpty(myList[0])+""+cardIsEmpty(myList[1])
//                    +""+cardIsEmpty(myList[2])+""+cardIsEmpty(myList[3])+""+cardIsEmpty(myList[4]))

            // restore previous enemy hand
            IntArraySettingCardArray(savedEnemy, enemyList)
//            Log.d("EnemyHand", ""+cardIsEmpty(enemyList[0])+""+cardIsEmpty(enemyList[1])
//                    +""+cardIsEmpty(enemyList[2])+""+cardIsEmpty(enemyList[3])+""+cardIsEmpty(enemyList[4]))

            //restore board
            IntArraySettingBoardArray(savedBoard)
//            Log.d("Board Row 1", " "+spotIsEmpty(0,0) + " "+spotIsEmpty(0,1) + " "+spotIsEmpty(0,2))
//            Log.d("Board Row 2", " "+spotIsEmpty(1,0) + " "+spotIsEmpty(1,1) + " "+spotIsEmpty(1,2))
//            Log.d("Board Row 3", " "+spotIsEmpty(2,0) + " "+spotIsEmpty(2,1) + " "+spotIsEmpty(2,2))
        }
        else {
            // randomly give cards if no saved data
            fillList(playerList)
            fillList(enemyList)
        }

        // fills both hands with cards
        var playerCardOne = playerList[0]
        var playerCardTwo = playerList[1]
        var playerCardThree = playerList[2]
        var playerCardFour = playerList[3]
        var playerCardFive = playerList[4]

        var enemyCardOne = enemyList[0]
        var enemyCardTwo = enemyList[1]
        var enemyCardThree = enemyList[2]
        var enemyCardFour = enemyList[3]
        var enemyCardFive = enemyList[4]


        // link cards to xml imageViews
        playerHandOne = findViewById(R.id.player_card_one)
        playerHandTwo = findViewById(R.id.player_card_two)
        playerHandThree = findViewById(R.id.player_card_three)
        playerHandFour = findViewById(R.id.player_card_four)
        playerHandFive = findViewById(R.id.player_card_five)

        enemyHandOne = findViewById(R.id.opponent_card_one)
        enemyHandTwo = findViewById(R.id.opponent_card_two)
        enemyHandThree = findViewById(R.id.opponent_card_three)
        enemyHandFour = findViewById(R.id.opponent_card_four)
        enemyHandFive = findViewById(R.id.opponent_card_five)


        // cards in both hands are set to the corresponding images
        playerHandOne?.setImageResource(playerCardOne.imageId)
        playerHandTwo?.setImageResource(playerCardTwo.imageId)
        playerHandThree?.setImageResource(playerCardThree.imageId)
        playerHandFour?.setImageResource(playerCardFour.imageId)
        playerHandFive?.setImageResource(playerCardFive.imageId)

        enemyHandOne?.setImageResource(enemyCardOne.imageId)
        enemyHandTwo?.setImageResource(enemyCardTwo.imageId)
        enemyHandThree?.setImageResource(enemyCardThree.imageId)
        enemyHandFour?.setImageResource(enemyCardFour.imageId)
        enemyHandFive?.setImageResource(enemyCardFive.imageId)


        // make player cards draggable
        if(playerCardOne.imageId != R.drawable.emptyslot)
            makeDragger(playerHandOne, playerCardOne)
        if(playerCardTwo.imageId != R.drawable.emptyslot)
            makeDragger(playerHandTwo, playerCardTwo)
        if(playerCardThree.imageId != R.drawable.emptyslot)
            makeDragger(playerHandThree, playerCardThree)
        if(playerCardFour.imageId != R.drawable.emptyslot)
            makeDragger(playerHandFour, playerCardFour)
        if(playerCardFive.imageId != R.drawable.emptyslot)
            makeDragger(playerHandFive, playerCardFive)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        savedEnemy = cardArrayToIntArray(enemyList)
        savedPlayer = cardArrayToIntArray(playerList)
        savedBoard = boardStateToIntArray()

        outState.putBoolean("SavedMusic", musicPlaying)
        outState.putIntArray("SavedEnemy", savedEnemy)
        outState.putIntArray("SavedPlayer", savedPlayer)
        outState.putIntArray("SavedBoard", savedBoard)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        super.onBackPressed()
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
//                    if(draggedCard.imageId == R.drawable.cardback)
//                        Log.d("dragListener", "dragged card's image is its back")
                    placeCard(draggedCard, row, col)
//                    Log.d("D", "North:" + draggedNumbers[1] + " East:" + draggedNumbers[2] + " South:" + draggedNumbers[3] + " West:" + draggedNumbers[4])
//                    Log.d("D", "Player placed card at square: " + row +", " + col)
                    destination.setImageResource(draggedCard.imageId)
                    destination.setBackgroundColor(resources.getColor(currentColor))
                    //v.visibility = View.INVISIBLE
                    v.setImageResource(R.drawable.emptyslot)
                    removeFromPlayerHand(v)
                    v.setEnabled(false)

                    // delay the AI by 1 - 3 seconds
                    val delay: Int = (1000..3000).random()
                    unmakeDragger()
                    Handler(Looper.getMainLooper()).postDelayed({
                        playAI()
                        remakeDraggers()
                    }, delay.toLong())
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
            val clipText = "" + card.imageId + "," + card.northVal + "," + card.eastVal + "," + card.southVal + "," + card.westVal
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(view)
            view.startDragAndDrop(data, dragShadowBuilder, view, 0)
            //view.visibility = View.INVISIBLE
            false
        }
        view?.setOnClickListener{
            Toast.makeText(this, "LONG PRESS A CARD TO MOVE IT", Toast.LENGTH_SHORT).show()
        }
        //TODO: get motion gesture to work(counts as one of the optional features)
        // turning touch listener back on will invalidate both onclick listeners
//        view?.setOnTouchListener{ v, event ->
//            val action = event.action
//            when(action){
//                MotionEvent.ACTION_DOWN -> {
//
//                }
//            }
//            true
//        }
    }

    // AI Logic handling
    fun playAI(){
        if(checkBoardFull()){
            return
        }
        var cardIndex = randomHand()
        var card = enemyList[cardIndex]
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

        val view = cordToSpace(row, col)

        if(cardsPlaced % 2 == 0){
            currentColor = R.color.blue
        }
        else{
            currentColor = R.color.red
        }

        val cardCopy = newCardCopy(card)
        cardCopy.color = 1



        placeCard(cardCopy, row, col)
        cardView?.setImageResource(R.drawable.emptyslot)
        view?.setImageResource(card.imageId)
        view?.setBackgroundColor(resources.getColor(currentColor))
        // changes to card in board are reflected in card in enemyList
        board[row][col].color = 1
        setCardToEmpty(enemyList[cardIndex])
    }

    // AI will play its hand sequentially
    fun linearHand(): Int{
        return cardsPlaced / 2
    }

    // AI will play its hand randomly
    fun randomHand(): Int{
        var result = (0..4).random()
        var card = indexToCardViewAI(result)

        // AI only checks the imageView's current image resource
        while(card?.drawable?.constantState?.equals
                (resources.getDrawable(R.drawable.emptyslot).constantState)!!){
            result = (0..4).random()
            card = indexToCardViewAI(result)

        }

        return result
    }

    // TODO: AI will play its hand optimally
    fun optimalHand(): Int{
        return -1
    }

    // returns the enemy hand corresponding to the index
    fun indexToCardViewAI(index: Int): ImageView? {
        when(index){
            0 -> {
                return enemyHandOne
            }
            1 -> {
                return enemyHandTwo
            }
            2 -> {
                return enemyHandThree
            }
            3 -> {
                return enemyHandFour
            }
            4 -> {
                return enemyHandFive
            }
        }

        return enemyHandOne
    }

    // returns the board space corresponding to the row and column
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
//            Log.d("placeCard", "space is taken")
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

//        Log.d("Score", "player:" + playerScore + " ai:" + AIScore)
        if(isFull){
            return false
        }

//        Log.d("D", "Board is filled, game is over")
        if(playerPoints > AIPoints) {
//            Log.d("D", "Player WIN")
            Toast.makeText(this, getString(R.string.you_win), Toast.LENGTH_LONG).show()
        }
        else if(playerPoints == AIPoints) {
//            Log.d("D", "TIE")
            Toast.makeText(this, getString(R.string.you_tied), Toast.LENGTH_LONG).show()
        }
        else {
//            Log.d("D", "Player LOSE")
            Toast.makeText(this, getString(R.string.you_lose), Toast.LENGTH_LONG).show()
        }
        return true
    }

    // check if coordinates are within the board space
    fun isWithinBounds(row: Int, col: Int): Boolean {
        return row != 3 && row != -1 && col != 3 && col != -1
    }

    // check if the neighboring cards are of opposite colors
    fun isOpponentCard(row: Int, col: Int, playerColor: Int): Boolean {
        return board[row][col].color != playerColor && board[row][col].color != -1
    }

    // flips other cards if the placed card is able to
    fun interactOtherCards(row: Int, col: Int, card: Card){
        board[row][col] = card
        val thisColor = card.color
        // check north side
        if(isWithinBounds(row - 1, col)){
//            Log.d("D", "North Interaction")
            if(card.northVal > board[row - 1][col].southVal && isOpponentCard(row - 1, col, thisColor)){
//                Log.d("D", "North flip")
                board[row - 1][col].color = thisColor
                val northView = cordToSpace(row - 1, col)
                northView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }
        // check east side
        if(isWithinBounds(row, col + 1)){
//            Log.d("D", "east Interaction")
            if(card.eastVal > board[row][col + 1].westVal && isOpponentCard(row, col + 1, thisColor)){
//                Log.d("D", "east flip")
                board[row][col + 1].color = thisColor
                val eastView = cordToSpace(row, col + 1)
                eastView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }
        // check south side
        if(isWithinBounds(row + 1, col)){
//            Log.d("D", "south Interaction")
            if(card.southVal > board[row + 1][col].northVal  && isOpponentCard(row + 1, col, thisColor)){
//                Log.d("D", "south flip")
                board[row + 1][col].color = thisColor
                val southView = cordToSpace(row + 1, col)
                southView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }
        // check west side
        if(isWithinBounds(row, col - 1)){
//            Log.d("D", "west Interaction")
            if(card.westVal > board[row][col - 1].eastVal && isOpponentCard(row, col - 1, thisColor)){
//                Log.d("D", "west flip")
                board[row][col - 1].color = thisColor
                val westView = cordToSpace(row, col - 1)
                westView?.setBackgroundColor(resources.getColor(currentColor))
            }
        }
    }

    // deals out random cards for the respective hand
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

    // translates listCard data to make a new card object
    fun listCardToCard(listCard: ListCard) : Card{
        return Card(listCard.imageResourceId, listCard.name,
            listCard.north, listCard.east, listCard.south, listCard.west)
    }

    // translates int array data to make a new card object
    fun arrayToCard(numbers: IntArray): Card{
        return Card(numbers[0], "DEFAULT", numbers[1], numbers[2], numbers[3], numbers[4])
    }

    // AI places its card sequentially from top left to bottom right
    fun AILinear(): IntArray{
        for(row in 0..2){
            for(col in 0..2){
                if(spotIsEmpty(row, col)){
//                    Log.d("D", "AI Found row:" + row + " col:"+ col +" to be empty.")
                    return intArrayOf(row, col)
                }
            }
        }

        return intArrayOf(-1, -1)
    }

    // AI places its card randomly
    fun AIRandom(): IntArray{
        var row = (0..2).random()
        var col = (0..2).random()
        while(!spotIsEmpty(row, col)){
            row = (0..2).random()
            col = (0..2).random()
        }

//        Log.d("D", "AI Found row:" + row + " col:"+ col +" to be empty.")
        return intArrayOf(row, col)
    }

    // duplicate of isOpponentCard, TODO: REFACTOR
    fun isEnemy(myCard: Card, otherCard: Card): Boolean{
        return myCard.color != otherCard.color && otherCard.color != -1
    }

    // AI tries to find and place its card where it finds the first +1 capture
    fun AIOptimal(card : Card): IntArray{
        for (row in 0..2) {
            for (col in 0..2) {
                if (spotIsEmpty(row, col)) {
                    if(isWithinBounds(row - 1, col) && !spotIsEmpty(row - 1, col)){
                        var otherCard = board[row - 1][col]
//                        Log.d("D", "North Interaction")
                        if(card.northVal > otherCard.southVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }

                    // check east side
                    if(isWithinBounds(row, col + 1) && !spotIsEmpty(row, col + 1)){
                        var otherCard = board[row][col + 1]
//                        Log.d("D", "east Interaction")
                        if(card.eastVal > otherCard.westVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }

                    // check south side
                    if(isWithinBounds(row + 1, col) && !spotIsEmpty(row + 1, col)){
                        var otherCard = board[row + 1][col]
//                        Log.d("D", "south Interaction")
                        if(card.southVal > otherCard.northVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }

                    // check west side
                    if(isWithinBounds(row, col - 1)  && !spotIsEmpty(row, col - 1)){
                        var otherCard = board[row][col - 1]
//                        Log.d("D", "west Interaction")
                        if(card.westVal > otherCard.eastVal && isEnemy(card, otherCard)){
                            return intArrayOf(row, col)
                        }
                    }
                }
            }
        }
        return intArrayOf(-1, -1)
    }

    // returns true if a card can be placed at this spot in the board
    fun spotIsEmpty(row: Int, col: Int): Boolean{
        return board[row][col].color == -1
    }

    // packages the player's hand to an int array
    fun cardArrayToIntArray(thisList: Array<Card>): IntArray{
        var result = IntArray(30)
        var resultIndex = 0

        for(i in 0..4){
            result[resultIndex] = thisList[i].imageId
            resultIndex++
            result[resultIndex] = thisList[i].color
            resultIndex++
            result[resultIndex] = thisList[i].northVal
            resultIndex++
            result[resultIndex] = thisList[i].eastVal
            resultIndex++
            result[resultIndex] = thisList[i].southVal
            resultIndex++
            result[resultIndex] = thisList[i].westVal
            resultIndex++
        }

        return result
    }

    // packages the board to an int array
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

    // unpackages an int array and puts it into the target's hand
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


    // unpackages an int array and puts it into the board
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


                var view = cordToSpace(row, col)
                view?.setImageResource(board[row][col].imageId)
                if(board[row][col].color == 1)
                    view?.setBackgroundColor(resources.getColor(R.color.red))
                else if(board[row][col].color == 0)
                    view?.setBackgroundColor(resources.getColor(R.color.blue))
            }
        }
    }

    // set the card's image to be empty
    fun setCardToEmpty(card: Card){
        card.imageId = R.drawable.emptyslot
        //card.color = -1
        //card.northVal = -1
        //card.eastVal = -1
        //card.southVal = -1
        //card.westVal = -1
    }

    // returns true if the card's image is empty
    fun cardIsEmpty(card: Card): Boolean{
        return card.imageId == R.drawable.emptyslot
    }

    // removes the card from player hand once it's placed on the board
    fun removeFromPlayerHand(card: ImageView){
        val cardImage = card?.drawable?.constantState
        when(card){
            playerHandOne ->{
                setCardToEmpty(playerList[0])
                playerHandOne?.setEnabled(false)
//                playerCardOne?.setImageResource(R.drawable.emptyslot)
            }

            playerHandTwo ->{
                setCardToEmpty(playerList[1])
                playerHandTwo?.setEnabled(false)
//                playerCardTwo?.setImageResource(R.drawable.emptyslot)
            }

            playerHandThree ->{
                setCardToEmpty(playerList[2])
                playerHandThree?.setEnabled(false)
//                playerCardThree?.setImageResource(R.drawable.emptyslot)
            }

            playerHandFour ->{
                setCardToEmpty(playerList[3])
                playerHandFour?.setEnabled(false)
//                playerCardFour?.setImageResource(R.drawable.emptyslot)
            }

            playerHandFive ->{
                setCardToEmpty(playerList[4])
                playerHandFive?.setEnabled(false)
//                playerCardFive?.setImageResource(R.drawable.emptyslot)
            }

            else ->{
                Log.d("removeFromPlayerHand", "defaulted to else statement")
            }
        }
    }

    // makes a deep copy of a card
    fun newCardCopy(card: Card): Card{
        return Card(card.imageId, "DEFAULT", card.northVal,
                        card.eastVal, card.southVal, card.westVal)
    }

    // enables and disables the player hand's interactability
    fun unmakeDragger(){
        playerHandOne?.setOnLongClickListener{
            true
        }
        playerHandTwo?.setOnLongClickListener{
            true
        }
        playerHandThree?.setOnLongClickListener{
            true
        }
        playerHandFour?.setOnLongClickListener{
            true
        }
        playerHandFive?.setOnLongClickListener{
            true
        }
    }

    fun remakeDraggers(){
        makeDragger(playerHandOne, playerList[0])
        makeDragger(playerHandTwo, playerList[1])
        makeDragger(playerHandThree, playerList[2])
        makeDragger(playerHandFour, playerList[3])
        makeDragger(playerHandFive, playerList[4])
    }

}