package com.animesearch.repository

import android.util.Log
import com.animesearch.model.ResultModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class SearchRepositoryImpl : SearchRepository {
    override suspend fun searchImage(file: File): List<ResultModel> = withContext(Dispatchers.IO) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            .build()

        val request = Request.Builder()
            .url("https://api.trace.moe/search")
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                parseResults(responseBody)
            } else {
                Log.e("SearchRepository", "Error: ${response.message}")
                emptyList()
            }
        } catch (e: IOException) {
            Log.e("SearchRepository", "Error: ${e.message}")
            emptyList()
        }
    }

    private fun parseResults(responseBody: String): List<ResultModel> {
        val resultsArray = JSONObject(responseBody).getJSONArray("result")
        return List(resultsArray.length()) { index ->
            val result = resultsArray.getJSONObject(index)
            ResultModel(
                preview = result.optString("image"),
                filename = result.optString("filename"),
                episode = result.optString("episode")
            )
        }
    }
}