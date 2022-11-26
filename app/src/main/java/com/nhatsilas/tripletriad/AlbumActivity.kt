package com.nhatsilas.tripletriad

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nhatsilas.tripletriad.adapter.CardAdapter
import com.nhatsilas.tripletriad.databinding.ActivityAlbumBinding

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

        var backBtn : ImageView = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        // if landscape mode, change span to 3
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            var recyclerView: RecyclerView = findViewById(R.id.album_recycler_view)
            recyclerView.layoutManager = GridLayoutManager(this, 3)
        }
    }
}