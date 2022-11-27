package com.nhatsilas.tripletriad

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nhatsilas.tripletriad.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val title: TextView = findViewById(R.id.title)


//         restores the language
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var savedLanguage = Locale(sharedPreferences.getString("language", "en")!!)

        Log.d("Main Activity Language", sharedPreferences.getString("language", "en")!!)
        Locale.setDefault(savedLanguage!!)
        resources.configuration.locale = savedLanguage
        resources.configuration.setLayoutDirection(savedLanguage)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        if(sharedPreferences.getString("language", "en").equals("es")) {
//            Toast.makeText(this, "Previously saved Spanish: Restoring...", Toast.LENGTH_SHORT).show()
            if(!title.text.toString().equals(resources.getString(R.string.welcome_main))){
//                Toast.makeText(this, "Title Translation MISMATCH: Changing...", Toast.LENGTH_SHORT).show()
                title.text = resources.getString(R.string.welcome_main)
            }
        }
        else{
//            Toast.makeText(this, "Previously saved English: Restoring...", Toast.LENGTH_SHORT).show()
        }


        // Launch the PlayActivity on playBtn click
        binding.play.setOnClickListener { launchPlay() }

        // Launch the AlbumActivity on albumBtn click
        binding.album.setOnClickListener { launchAlbum() }

        // Launch the SettingsActivity on settingsBtn click
        binding.settings.setOnClickListener { launchSettings() }

        // Launch the RulebookActivity on settingsBtn click
        binding.rulebook.setOnClickListener { launchRulebook() }
    }



    private fun launchPlay() {
        listIntent = Intent(this, PlayActivity::class.java)
        startActivity(listIntent)
        finish()
    }

    private fun launchAlbum() {
        listIntent = Intent(this, AlbumActivity::class.java)
        startActivity(listIntent)
        finish()
    }

    private fun launchSettings() {
        listIntent = Intent(this, SettingsActivity::class.java)
        startActivity(listIntent)
        finish()
    }

    private fun launchRulebook() {
        listIntent = Intent(this, RulebookActivity::class.java)
        startActivity(listIntent)
        finish()
    }
}