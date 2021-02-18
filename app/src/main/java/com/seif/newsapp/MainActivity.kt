package com.seif.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
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
    private fun fadeInFromBlack(){
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }
    private fun setUpRecyclerView(){
        rec_news.layoutManager = LinearLayoutManager(applicationContext)
        rec_news.adapter = NewsRecyclerAdapter(titlelist, descriptionlist, imageslist, linkslist)
    }
    private fun addToList(title:String, description:String, image:String, link:String){
        titlelist.add(title)
        descriptionlist.add(description)
        imageslist.add(image)
        linkslist.add(link)
    }

    private fun makeApiRequest() {
        progressBar.visibility = View.VISIBLE
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
                    Log.d("main",article.toString()+"\n")
                    addToList(article.title, article.description, article.image, article.url)
                }
                // to update Ui
                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                    fadeInFromBlack()
                    progressBar.visibility = View.GONE

                }
            }
            catch (e:Exception){
                withContext(Dispatchers.Main){
                    attempRequestAgain()

                }
            }


        }
    }

    private fun attempRequestAgain() {
        countDownTimer = object : CountDownTimer(5000,1000){
            override fun onTick(millisUntilFinished: Long) {
                Log.d("main","coudn't retrieve data .... Trying again in ${millisUntilFinished/1000} seconds")

            }

            override fun onFinish() {
                makeApiRequest()
                // we have to cancel our countdowntimer as sometimes it will reset it
                // only one request
                countDownTimer.cancel()
            }

        }.start()
    }
}