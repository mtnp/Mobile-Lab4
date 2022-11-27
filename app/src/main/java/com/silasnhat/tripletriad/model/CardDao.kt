package com.silasnhat.tripletriad.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCard(listCard: ListCard)

    @Query("SELECT * FROM card_table ORDER BY name ASC")
    fun readAllData(): LiveData<List<ListCard>>
}