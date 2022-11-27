package com.nhatsilas.tripletriad

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class SettingsActivity : AppCompatActivity() {
    var savedLanguage: Locale ?= null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var languageGroup: RadioGroup
    lateinit var engBtn: RadioButton
    lateinit var spaBtn: RadioButton
    lateinit var musicGroup: RadioGroup
    lateinit var musicOn: RadioButton
    lateinit var musicOff: RadioButton
    lateinit var shEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        shEditor = sharedPreferences.edit()

        musicGroup = findViewById(R.id.musicRadioGroup)
        musicOn = findViewById(R.id.onBtn)
        musicOff = findViewById(R.id.offBtn)
        musicOn.setOnClickListener {
            changeMusic(true)
        }
        musicOff.setOnClickListener {
            changeMusic(false)
        }

        if(sharedPreferences.getBoolean("musicPersist", true)) {
            Log.d("Settings", "Previously saved music ON")
            musicGroup.check(R.id.onBtn)
        }
        else{
            Log.d("Settings", "Previously saved music OFF")
            musicGroup.check(R.id.offBtn)
        }


        var backBtn : ImageView = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        languageGroup = findViewById(R.id.languageRadioGroup)
        engBtn = findViewById(R.id.englishBtn)
        spaBtn = findViewById(R.id.spanishBtn)

        savedLanguage = Locale(sharedPreferences.getString("language", "en")!!)
        Locale.setDefault(savedLanguage!!)
        resources.configuration.locale = savedLanguage
        resources.configuration.setLayoutDirection(savedLanguage)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        if(sharedPreferences.getString("language", "en").equals("es")) {
            languageGroup.check(R.id.spanishBtn)
        }
        else{
            languageGroup.check(R.id.englishBtn)
        }

        engBtn.setOnClickListener{
            changeLanguageTo("en")
        }

        spaBtn.setOnClickListener{
            changeLanguageTo("es")
        }

        restoreRadioOnRotate()

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
//        finish()
    }

    fun changeLanguageTo(language: String){
        val locale = Locale(language)

        when(language){
            "en" ->{
                shEditor.putString("language", language)
            }
            "es" ->{
                shEditor.putString("language", language)
            }
            else ->{
                Log.d("changeLanguageTo", "Something bad happened")
            }
        }


//        LocaleHelper.setLocale(this, language)

        shEditor.apply()

        Locale.setDefault(locale)
        resources.configuration.locale = locale
        resources.configuration.setLayoutDirection(locale)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        findViewById<TextView>(R.id.Language).text = resources.getString(R.string.language)
        findViewById<TextView>(R.id.BGM).text = resources.getString(R.string.music)
        findViewById<RadioButton>(R.id.onBtn).text = resources.getString(R.string.on)
        findViewById<RadioButton>(R.id.offBtn).text = resources.getString(R.string.off)

//        setLocale(language)

    }

    fun changeMusic(bool: Boolean){
        shEditor.putBoolean("musicPersist", bool)
        shEditor.apply()
    }

    fun restoreRadioOnRotate(){
        when(languageGroup.checkedRadioButtonId){
            R.id.englishBtn -> {
                Log.d("Restore Radio", "english")
            }
            R.id.spanishBtn -> {
                Log.d("Restore Radio", "spanish")
            }
            else ->{
                Log.d("Restore Radio", "error")
            }
        }
        findViewById<TextView>(R.id.Language).text = resources.getString(R.string.language)
        findViewById<TextView>(R.id.BGM).text = resources.getString(R.string.music)
        findViewById<RadioButton>(R.id.onBtn).text = resources.getString(R.string.on)
        findViewById<RadioButton>(R.id.offBtn).text = resources.getString(R.string.off)
    }

}