package com.example.tripletriad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val musicToggle = findViewById<TextView>(R.id.BGM)
        musicToggle.setOnClickListener {
            MusicPlayer.mpStop()
        }
    }
}