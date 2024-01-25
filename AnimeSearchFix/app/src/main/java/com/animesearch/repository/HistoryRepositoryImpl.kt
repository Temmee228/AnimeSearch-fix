package com.animesearch.repository

import com.animesearch.database.HistoryDao
import com.animesearch.model.HistoryModel

class HistoryRepositoryImpl(private val dao: HistoryDao) : HistoryRepository {
    override fun insertHistory(historyModels: List<HistoryModel>) {
        return dao.insertHistory(historyModels)
    }

    override fun getHistory(): List<HistoryModel> {
        return dao.getHistory()
    }

    override fun clearHistory() {
        return dao.clearHistory()
    }
}