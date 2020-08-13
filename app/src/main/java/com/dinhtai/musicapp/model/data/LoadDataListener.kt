package com.dinhtai.musicapp.model.data

import com.dinhtai.musicapp.model.entity.Music

interface LoadDataListener {
    fun onLoadData(listMusic : MutableList<Music>)
}