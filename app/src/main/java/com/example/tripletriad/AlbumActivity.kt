package com.example.tripletriad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tripletriad.adapter.CardAdapter
import com.example.tripletriad.databinding.ActivityAlbumBinding

class AlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.albumRecyclerView.adapter = CardAdapter(applicationContext)

        // Specify fixed size to improve performance
        binding.albumRecyclerView.setHasFixedSize(true)

        // Enable up button for backward navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}