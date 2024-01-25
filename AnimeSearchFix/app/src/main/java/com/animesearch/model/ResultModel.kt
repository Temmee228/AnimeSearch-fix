package com.animesearch.model

data class ResultModel(
    val preview: String,
    val filename: String,
    val episode: String,
) {
    fun toHistoryModel(): HistoryModel {
        return HistoryModel(
            preview = preview,
            filename = filename,
            episode = episode
        )
    }
}
