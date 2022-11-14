package com.nhatsilas.tripletriad.model

import androidx.lifecycle.LiveData


class CardRepository(private val cardDao: CardDao) {
    val readAllData: LiveData<List<ListCard>> = cardDao.readAllData()

    suspend fun addCard(card: ListCard){
        cardDao.addCard(card)

    }
}