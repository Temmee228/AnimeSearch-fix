package com.animesearch.interactor

import com.animesearch.model.ResultModel
import java.io.File

interface SearchInteractor {
    suspend fun searchImage(file: File): List<ResultModel>
}