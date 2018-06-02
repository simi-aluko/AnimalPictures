package com.example.simileoluwaaluko.animalpictures

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.simileoluwaaluko.animalpictures.Models.AnimalResultModel
import com.example.simileoluwaaluko.animalpictures.Models.Hit
import com.squareup.picasso.Picasso
import java.net.URL

class AnimalPictureAdapter(private val context : Context,private val resource : List<Hit>) : RecyclerView.Adapter<AnimalPictureAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.animal_recycler_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageUrl = resource[position].webformatURL

        Picasso.get().load(imageUrl).into(holder.imageView)
        holder.imageTag.text = resource[position].tags
    }

    override fun getItemCount(): Int {
        return resource.size
    }


    inner class MyViewHolder(itemView : View) : ViewHolder(itemView) {
        val imageView = itemView.findViewById<AppCompatImageView>(R.id.animal_image)
        val imageTag = itemView.findViewById<TextView>(R.id.animal_tag)
    }
}