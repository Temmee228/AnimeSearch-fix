package com.animesearch.interactor

import com.animesearch.model.ResultModel
import com.animesearch.repository.SearchRepository
import java.io.File

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override suspend fun searchImage(file: File): List<ResultModel> {
        return searchRepository.searchImage(file)
    }
}