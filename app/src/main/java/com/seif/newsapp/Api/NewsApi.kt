package com.seif.newsapp.Api

data class NewsApi(
    val news: List<New>,
    val page: Int,
    val status: String
)