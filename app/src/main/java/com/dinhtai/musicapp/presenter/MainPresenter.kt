package com.dinhtai.musicapp.presenter

import android.content.Context
import com.dinhtai.musicapp.model.data.DataMusic
import com.dinhtai.musicapp.model.data.LoadDataListener
import com.dinhtai.musicapp.model.entity.Music
import com.dinhtai.musicapp.view.adapter.MainViewListener


class MainPresenter(
   var mainViewListener: MainViewListener, context: Context?
) : LoadDataListener {
    var dataMusic: DataMusic
    var listMusic : MutableList<Music>
    init {
        dataMusic = DataMusic(context,this)
        listMusic = ArrayList<Music>()
    }
    fun loadData() {
        dataMusic.getMusicData()
    }

    override fun onLoadData(listMusic: MutableList<Music>) {
        mainViewListener.onDisplayView(listMusic)
    }
}