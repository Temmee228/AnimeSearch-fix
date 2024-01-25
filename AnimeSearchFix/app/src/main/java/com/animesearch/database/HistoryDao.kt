package com.animesearch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.animesearch.model.HistoryModel

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(historyModels: List<HistoryModel>)

    @Query("SELECT * FROM table_history")
    fun getHistory(): List<HistoryModel>

    @Query("DELETE FROM table_history")
    fun clearHistory()
}