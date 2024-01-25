package com.animesearch.interactor

import com.animesearch.model.HistoryModel
import com.animesearch.repository.HistoryRepository

class HistoryInteractorImpl(private val repository: HistoryRepository) : HistoryInteractor {
    override suspend fun insertHistory(historyModels: List<HistoryModel>) {
        return repository.insertHistory(historyModels)
    }

    override suspend fun getHistory(): List<HistoryModel> {
        return repository.getHistory()
    }

    override suspend fun clearHistory() {
        return repository.clearHistory()
    }
}