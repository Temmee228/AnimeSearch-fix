package com.animesearch.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "table_history",
    indices = [Index(value = ["preview", "filename", "episode"], unique = true)]
)
data class HistoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val preview: String,
    val filename: String,
    val episode: String,
)
