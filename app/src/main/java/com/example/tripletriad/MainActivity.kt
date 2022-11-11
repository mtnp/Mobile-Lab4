package com.example.tripletriad

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tripletriad.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listIntent: Intent
//    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Launch the PlayActivity on playlBtn click
        binding.playBtn.setOnClickListener { launchPlay() }

        // Launch the AlbumActivity on albumBtn click
        binding.albumBtn.setOnClickListener { launchAlbum() }

        // Launch the SettingsActivity on settingsBtn click
        binding.settingsBtn.setOnClickListener { launchSettings() }

        // Launch the RulebookActivity on settingsBtn click
        binding.rulebookBtn.setOnClickListener { launchRulebook() }
//        playMusic()

        //startService(Intent(this, MusicPlayer::class.java))
    }

//    override fun onPause() {
//        stopService(Intent(this, MusicPlayer::class.java))
//        super.onPause()
//    }
//
//    override fun onRestart() {
//        startService(Intent(this, MusicPlayer::class.java))
//        super.onRestart()
//    }

    private fun launchPlay() {
        listIntent = Intent(this, PlayActivity::class.java)
        startActivity(listIntent)
    }

    private fun launchAlbum() {
        listIntent = Intent(this, AlbumActivity::class.java)
        startActivity(listIntent)
    }

    private fun launchSettings() {
        listIntent = Intent(this, SettingsActivity::class.java)
        startActivity(listIntent)
    }

    private fun launchRulebook() {
        listIntent = Intent(this, RulebookActivity::class.java)
        startActivity(listIntent)
    }

//    private fun playMusic() {
//        mediaPlayer.setDataSource(this, Uri.parse(
//            "android.resource://" + this.packageName + "/" + R.raw.song))
//        mediaPlayer.prepare()
//        mediaPlayer.start()
//    }
}