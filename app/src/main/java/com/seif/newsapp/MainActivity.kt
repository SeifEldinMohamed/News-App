package com.seif.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.news_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BaseUrl = "https://api.currentsapi.services"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)


        makeApiRequest()


    }

    private fun makeApiRequest() {
        val api = Retrofit.Builder()
            .baseUrl(BaseUrl) // take base url for the api
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java) // takes the interface

        // we add the Dispatchers.Io to handle the information
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()
                for (article in response.news){
                    Log.d("main","data = $article")
                }
            }
            catch (e:Exception){
                Toast.makeText(this@MainActivity, "$e", Toast.LENGTH_SHORT).show()
            }

        }
    }
}