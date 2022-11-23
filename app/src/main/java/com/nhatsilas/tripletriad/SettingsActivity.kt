package com.nhatsilas.tripletriad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        val musicOn: RadioButton = findViewById(R.id.onBtn)
        val musicOff: RadioButton = findViewById(R.id.offBtn)
        musicOn.setOnClickListener {
            MusicPlayer.mpStop()
        }

        var backBtn : ImageView = findViewById(R.id.backBtn)
        backBtn.setOnClickListener{
            startActivity(parentActivityIntent)
        }

//        musicOff.setOnClickListener {
//            MusicPlayer.playSound()
//        }

        val engBtn: RadioButton = findViewById(R.id.englishBtn)
        val spaBtn: RadioButton = findViewById(R.id.spanishBtn)

        engBtn.setOnClickListener{
            val locale = Locale("en")
            Locale.setDefault(locale)

            resources.configuration.locale = locale
            resources.configuration.setLayoutDirection(locale)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)


        }
        spaBtn.setOnClickListener{
            val locale = Locale("es")
            Locale.setDefault(locale)

            resources.configuration.locale = locale
            resources.configuration.setLayoutDirection(locale)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        }
    }
}