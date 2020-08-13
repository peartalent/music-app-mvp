package com.dinhtai.musicapp.view.adapter

import com.dinhtai.musicapp.model.entity.Music

interface MainViewListener {
    fun onDisplayView(musics : MutableList<Music>)
}