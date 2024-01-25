package com.animesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesearch.interactor.HistoryInteractor
import com.animesearch.model.HistoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyInteractor: HistoryInteractor) : ViewModel() {
    private val _historyList = MutableLiveData<List<HistoryModel>>()
    val historyList: LiveData<List<HistoryModel>> = _historyList

    fun loadHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val historyItems = historyInteractor.getHistory()
            _historyList.postValue(historyItems)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            historyInteractor.clearHistory()
            loadHistory()
        }
    }
}