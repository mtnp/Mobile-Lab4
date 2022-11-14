package com.nhatsilas.tripletriad.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardViewModel(application: Application): AndroidViewModel(application) {
    private val readAllData: LiveData<List<ListCard>>
    private val repository: CardRepository

    init {
        val cardDao = CardDatabase.getDatabase(application).cardDao()
        repository = CardRepository(cardDao)
        readAllData = repository.readAllData
    }

    fun addCard(card: ListCard){
        viewModelScope.launch(Dispatchers.IO){
            repository.addCard(card)
        }
    }
}