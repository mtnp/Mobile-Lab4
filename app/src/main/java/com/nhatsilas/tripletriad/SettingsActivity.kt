package com.nhatsilas.tripletriad

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class SettingsActivity : AppCompatActivity() {
    var superLocale: Locale ?= null
    var localeString: String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        val musicOn: RadioButton = findViewById(R.id.onBtn)
        val musicOff: RadioButton = findViewById(R.id.offBtn)
        musicOn.setOnClickListener {
            MusicPlayer.mpStop()
        }

        var backBtn : ImageView = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

//        musicOff.setOnClickListener {
//            MusicPlayer.playSound()
//        }

        val languageGroup: RadioGroup = findViewById(R.id.languageRadioGroup)
        val engBtn: RadioButton = findViewById(R.id.englishBtn)
        val spaBtn: RadioButton = findViewById(R.id.spanishBtn)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val shEditor = sharedPreferences.edit()

        superLocale = Locale(sharedPreferences.getString("language", "en"))
        Locale.setDefault(superLocale)
        resources.configuration.locale = superLocale
        resources.configuration.setLayoutDirection(superLocale)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)



        engBtn.setOnClickListener{
            val locale = Locale("en")
            localeString = "en"
            superLocale = Locale(localeString)
            shEditor.putString("language", "es")
            Locale.setDefault(locale)

            resources.configuration.locale = locale
            resources.configuration.setLayoutDirection(locale)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)
        }

        spaBtn.setOnClickListener{
            val locale = Locale("es")
            localeString = "es"
            superLocale = Locale(localeString)
            shEditor.putString("language", "es")
            Locale.setDefault(locale)

            resources.configuration.locale = locale
            resources.configuration.setLayoutDirection(locale)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        resources.configuration.locale = superLocale
        resources.configuration.setLayoutDirection(superLocale)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
    }
}