package com.animesearch.repository

import com.animesearch.model.ResultModel
import java.io.File

interface SearchRepository {
    suspend fun searchImage(file: File): List<ResultModel>
}