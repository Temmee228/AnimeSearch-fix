package com.animesearch.interactor

import com.animesearch.model.HistoryModel

interface HistoryInteractor {
    suspend fun insertHistory(historyModels: List<HistoryModel>)
    suspend fun getHistory(): List<HistoryModel>
    suspend fun clearHistory()
}