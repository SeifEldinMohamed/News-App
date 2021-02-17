package com.seif.newsapp

import com.seif.newsapp.Api.NewsApi
import retrofit2.http.GET

interface ApiRequest {
    // get request from api
    // the part after base url
    @GET("/v1/search?Keywords=Trump?language=us&apiKey=t5LIkQw-NlWntQbFmi9nRb5ssKTHP2of0G8izFlpbWmdhSxq")
    suspend fun getNews():NewsApi
}