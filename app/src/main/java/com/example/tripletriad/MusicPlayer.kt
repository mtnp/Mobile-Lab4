package com.example.tripletriad

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle

object MusicPlayer {
    private lateinit var mp: MediaPlayer
    var playing: Boolean = false
    fun playSound(context: Context) {
        mp = MediaPlayer.create(context, R.raw.song)
        mp.isLooping = true
        mpStart()
    }

    fun mpStart() {
        if (!playing) {
            mp.start()
            playing = true
        }
    }

    fun mpStop() {
        if (playing) {
            mp.stop()
            playing = false
        }
    }


}