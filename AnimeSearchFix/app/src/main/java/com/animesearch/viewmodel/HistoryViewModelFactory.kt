package com.animesearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.animesearch.interactor.HistoryInteractor

class HistoryViewModelFactory(private val interactor: HistoryInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}