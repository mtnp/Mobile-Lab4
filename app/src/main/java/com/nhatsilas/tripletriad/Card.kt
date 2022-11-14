package com.nhatsilas.tripletriad

class Card (imageId: Int = R.drawable.cardback, name: String = "DEFAULT", northVal: Int = -1,
            eastVal: Int = -1, southVal: Int = -1, westVal: Int = -1){


    var imageId = imageId
        get() = field
        set(value){
            field = value
        }

    var color:Int = -1 // -1 is none, 0 is blue, and 1 is red
        get() = field
        set(value){
            field = value
        }

    var northVal = northVal
        get() = field
        set(value){
            field = value
        }

    var eastVal = eastVal
        get() = field
        set(value){
            field = value
        }

    var southVal = southVal
        get() = field
        set(value){
            field = value
        }

    var westVal = westVal
        get() = field
        set(value){
            field = value
        }



}