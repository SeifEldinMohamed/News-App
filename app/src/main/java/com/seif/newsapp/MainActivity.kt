package com.seif.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.seif.newsapp.Adapter.NewsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main1.*
import kotlinx.android.synthetic.main.news_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BaseUrl = "https://api.currentsapi.services"

class MainActivity : AppCompatActivity() {
    lateinit var countDownTimer: CountDownTimer
    private var titlelist = mutableListOf<String>()
    private var descriptionlist = mutableListOf<String>()
    private var imageslist = mutableListOf<String>()
    private var linkslist = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        makeApiRequest()

    }
    private fun setUpRecyclerView(){
        rec_news.layoutManager = LinearLayoutManager(this)
        rec_news.adapter = NewsRecyclerAdapter(titlelist, descriptionlist, imageslist, linkslist)
    }
    private fun addToList(title:String, description:String, image:String, link:String){
        titlelist.add(title)
        descriptionlist.add(description)
        imageslist.add(image)
        linkslist.add(link)
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
                    addToList(article.title, article.description, article.image, article.url)
                }
                // to update Ui
                withContext(Dispatchers.Main){
                    setUpRecyclerView()

                }
            }
            catch (e:Exception){
                Toast.makeText(this@MainActivity, "$e", Toast.LENGTH_SHORT).show()
            }

        }
    }
}