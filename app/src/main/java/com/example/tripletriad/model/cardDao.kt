package com.example.tripletriad.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface cardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCard(listCard: ListCard)

//    @Query("SELECT * FROM card_table ORDER BY name ASC")
}