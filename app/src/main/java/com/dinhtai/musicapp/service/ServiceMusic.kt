package com.dinhtai.musicapp.service

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.dinhtai.musicapp.model.data.DataMusic
import com.dinhtai.musicapp.model.data.HandleMusic
import com.dinhtai.musicapp.model.entity.Music
import com.dinhtai.musicapp.presenter.MainPresenter
import com.dinhtai.musicapp.view.adapter.MainViewListener



class ServiceMusic() : Service(), HandleMusic, MainViewListener {
    private var mediaPlayer: MediaPlayer? = null
    private var position = 0
    private var music: Music? = null
    private var listMusics: MutableList<Music> = mutableListOf()
    override fun onCreate() {
        super.onCreate()
        MainPresenter(this, this).loadData()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startMediaPlayer()
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = MyBinder()

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }


    inner class MyBinder : Binder() {
        fun getMusicControl(): ServiceMusic = this@ServiceMusic
    }

    fun getListMusic(): MutableList<Music>? = listMusics
    fun getCurrentPosition(): Int = position

    override fun createMediaPlayer(path1: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(path1)
        mediaPlayer?.prepare();
        mediaPlayer?.start()
    }
    fun createMediaPlayer(pos: Int) {
        position = pos
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(listMusics.get(pos).path)
        mediaPlayer?.prepare();
        mediaPlayer?.start()
    }
    override fun startMediaPlayer() {
        mediaPlayer?.let {
            stopMediaPlayer()
        }
        listMusics.get(position)?.let {
            createMediaPlayer(it.path)
        }
    }

    override fun resumeMediaPlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) it.pause()
            else it.start()
        }
    }

    override fun stopMediaPlayer() {
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    override fun nextMediaPlayer() {
        position++
        if (position >= listMusics.size) position = 0
        stopMediaPlayer()
        Log.v("Music App",position.toString())
        createMediaPlayer(listMusics.get(position).path)
    }

    override fun previousMediaPlayer() {
        position--
        if (position < 0) position = listMusics.size - 1
        Log.v("Music App",position.toString())
        stopMediaPlayer()
        createMediaPlayer(listMusics.get(position).path)

    }
    fun getNameSong(): String? = listMusics.get(position).name
    override fun getDurationMediaPlayer(): Int? = mediaPlayer?.duration

    override fun getCurrentPositionMediaPlayer(): Int? = mediaPlayer?.currentPosition
    fun getMediaPlayer(): MediaPlayer? = mediaPlayer
    override fun updateSeekToMediaPlayer(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun onDisplayView(musics: MutableList<Music>) {
        Log.v("MUSIC TAG: ", musics.toString())
        listMusics.addAll(musics)
        music=musics.get(position)
    }

}
