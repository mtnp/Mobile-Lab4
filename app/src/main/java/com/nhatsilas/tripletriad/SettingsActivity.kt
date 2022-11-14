package com.nhatsilas.tripletriad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val musicOn: RadioButton = findViewById(R.id.onBtn)
        val musicOff: RadioButton = findViewById(R.id.offBtn)
        musicOn.setOnClickListener {
            MusicPlayer.mpStop()
        }
//        musicOff.setOnClickListener {
//            MusicPlayer.playSound()
//        }
    }
}