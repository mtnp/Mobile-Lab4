package com.example.tripletriad

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder

object MusicPlayer {
    private lateinit var mp: MediaPlayer
    private var playing: Boolean = false
    private var pos: Int = 0

    fun playSound(context: Context) {
        mp = MediaPlayer.create(context, R.raw.song)
        mp.isLooping = true
        mpStart()
        mp.seekTo(pos)
    }

    fun mpStart() {
        if (!playing) {
            mp.start()
            playing = true
        }
    }

    fun mpStop() {
        if (playing) {
            mp.pause()
            pos = mp.currentPosition
            mp.stop()
            playing = false
        }
    }
}

//class MusicPlayer: Service() {
//    private lateinit var mp: MediaPlayer
//    private var playing: Boolean = false
//    private var pos: Int = 0
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        mp = MediaPlayer.create(this, R.raw.song)
//        mp.isLooping = true
//        mp.start()
//        mp.seekTo(pos)
//        return START_STICKY
//    }
//
////    override fun stopService(name: Intent?): Boolean {
////        return super.stopService(name)
////    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mp.stop()
//        mp.release()
//    }
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//    fun playSound(context: Context) {
//        mp = MediaPlayer.create(context, R.raw.song)
//        mp.isLooping = true
//        mpStart()
//        mp.seekTo(pos)
//    }
//
//    fun mpStart() {
//        if (!playing) {
//            mp.start()
//            playing = true
//        }
//    }
//
//    fun mpStop() {
//        if (playing) {
//            mp.pause()
//            pos = mp.currentPosition
//            mp.stop()
//            playing = false
//        }
//    }
