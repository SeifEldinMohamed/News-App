package com.seif.newsapp.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seif.newsapp.Api.New
import com.seif.newsapp.R
import kotlinx.android.synthetic.main.news_item.view.*

class NewsRecyclerAdapter(private var title : List<String>,
                          private var Discription : List<String>,
                          private var images : List<String>,
                          private var links : List<String>
                          ):RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        init {
            itemview.setOnClickListener {
                val position:Int = adapterPosition
                val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse(links[position]))
                itemview.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.txt_title.text = title[position]
        holder.itemView.txt_description.text = Discription[position]

        Glide.with(holder.itemView.image_news)
            .load(images[position])
            .into(holder.itemView.image_news)
    }

    override fun getItemCount(): Int {
        return title.size
    }
}