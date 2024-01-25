package com.animesearch.repository

import com.animesearch.model.HistoryModel

interface HistoryRepository {
    fun insertHistory(historyModels: List<HistoryModel>)
    fun getHistory(): List<HistoryModel>
    fun clearHistory()
}