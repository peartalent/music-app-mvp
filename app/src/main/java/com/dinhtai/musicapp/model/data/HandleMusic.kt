package com.dinhtai.musicapp.model.data

import android.media.MediaPlayer
import com.dinhtai.musicapp.model.entity.Music

interface HandleMusic {
    fun createMediaPlayer(path : String)
    fun startMediaPlayer()
    fun resumeMediaPlayer()
    fun stopMediaPlayer()
    fun nextMediaPlayer()
    fun previousMediaPlayer()
    fun getDurationMediaPlayer():Int?
    fun getCurrentPositionMediaPlayer(): Int?
    fun updateSeekToMediaPlayer(position : Int)
}
