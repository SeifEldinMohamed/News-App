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
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BaseUrl = "https://api.currentsapi.services"

class MainActivity : AppCompatActivity() {
   private var parentJob: Job = Job()
    private var titlelist = mutableListOf<String>()
    private var descriptionlist = mutableListOf<String>()
    private var imageslist = mutableListOf<String>()
    private var linkslist = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        makeApiRequest()

        swipeRefresh.setOnRefreshListener {
            makeApiRequest()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun fadeInFromBlack() {
        // show news in slow motion animation.
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun setUpRecyclerView() {
        rec_news.layoutManager = LinearLayoutManager(applicationContext)
        rec_news.adapter = NewsRecyclerAdapter(titlelist, descriptionlist, imageslist, linkslist)
    }

    private fun addToList(title: String, description: String, image: String, link: String) {
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
        parentJob = GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()
                for (article in response.news) {
                    Log.d("main", article.toString() + "\n")
                    addToList(article.title, article.description, article.image, article.url)
                }
                // to update Ui
                withContext(Dispatchers.Main) {
                    mainContainer.visibility = View.VISIBLE
                    setUpRecyclerView()
                    v_blackScreen.visibility = View.VISIBLE
                    fadeInFromBlack()
                    progressBar.visibility = View.GONE
                    txt_error.visibility = View.GONE

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    v_blackScreen.visibility = View.GONE
                    mainContainer.visibility = View.GONE
                    txt_error.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE

                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // to finish coroutine after activity closed
        parentJob.cancel()
    }
}
// don't forget to show a text message to user when there is no internet
// or you can show a button to user to refresh the page