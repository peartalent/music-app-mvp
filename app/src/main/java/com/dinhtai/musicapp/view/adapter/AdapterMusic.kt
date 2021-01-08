package com.dinhtai.musicapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dinhtai.musicapp.R
import com.dinhtai.musicapp.model.entity.Music
import kotlinx.android.synthetic.main.item.view.*

class AdapterMusic(var listMusic: MutableList<Music>,var onClickItemListener: OnClickItemListener) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false),onClickItemListener)
    }

    override fun getItemCount(): Int = listMusic.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var music = listMusic[position]
        holder.nameSong.text = music.name
    }
}
class ViewHolder (row : View, onClickItemListener: OnClickItemListener): RecyclerView.ViewHolder(row){
    var nameSong: TextView
    init {
        nameSong = row.txt_name
        nameSong.setOnClickListener {
            onClickItemListener.onClickItem(adapterPosition)
        }
    }
}