package com.animesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesearch.interactor.SearchInteractor
import com.animesearch.model.ResultModel
import kotlinx.coroutines.launch
import java.io.File

class SearchViewModel : ViewModel() {
    private val _resultList = MutableLiveData<List<ResultModel>>()
    val resultList: LiveData<List<ResultModel>> = _resultList

    fun setResultList(resultList: List<ResultModel>) {
        _resultList.postValue(resultList)
    }
}
