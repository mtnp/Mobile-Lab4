package com.nhatsilas.tripletriad

import android.content.Context
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
    lateinit var shEditor: SharedPreferences.Editor
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

        languageGroup = findViewById(R.id.languageRadioGroup)
        engBtn = findViewById(R.id.englishBtn)
        spaBtn = findViewById(R.id.spanishBtn)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        shEditor = sharedPreferences.edit()

        savedLanguage = Locale(sharedPreferences.getString("language", "en")!!)
        Locale.setDefault(savedLanguage!!)
        resources.configuration.locale = savedLanguage
        resources.configuration.setLayoutDirection(savedLanguage)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
//        Toast.makeText(this, "Hello There", Toast.LENGTH_SHORT).show()
        if(sharedPreferences.getString("language", "en").equals("es")) {
            Toast.makeText(this, "Previously saved Spanish: Restoring...", Toast.LENGTH_SHORT).show()
            languageGroup.check(R.id.spanishBtn)
        }
        else{
            Toast.makeText(this, "Previously saved English: Restoring...", Toast.LENGTH_SHORT).show()
        }



        engBtn.setOnClickListener{
            changeLanguageTo("en")
        }

        spaBtn.setOnClickListener{
            changeLanguageTo("es")
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
//        resources.configuration.locale = superLocale
//        resources.configuration.setLayoutDirection(superLocale)
//        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
    }

    fun changeLanguageTo(language: String){
        val locale = Locale(language)

        when(language){
            "en" ->{
                shEditor.putString("language", language)
//                findViewById<TextView>(R.id.Language).setText("Language")
//                findViewById<TextView>(R.id.BGM).setText("Music")
            }
            "es" ->{
                shEditor.putString("language", language)
//                findViewById<TextView>(R.id.Language).setText("Idioma")
//                findViewById<TextView>(R.id.BGM).setText("Musica")
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

    fun setLocale(lang: String?) {
        val myLocale = Locale(lang)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)
//        recreate()
    }

}