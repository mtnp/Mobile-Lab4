package com.example.tripletriad


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tripletriad.adapter.CardAdapter
import com.example.tripletriad.const.Layout
import com.example.tripletriad.databinding.ActivityCardListBinding

class CardListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        binding.verticalRecyclerView.adapter = DogCardAdapter(
            applicationContext,
            Layout.VERTICAL
        )

        // Specify fixed size to improve performance
        binding.verticalRecyclerView.setHasFixedSize(true)

        // Enable up button for backward navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}