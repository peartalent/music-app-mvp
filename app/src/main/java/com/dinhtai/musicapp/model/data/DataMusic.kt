package com.dinhtai.musicapp.model.data

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.dinhtai.musicapp.model.entity.Music
import java.lang.IllegalStateException
import java.text.SimpleDateFormat

class DataMusic(val context: Context?, val loadDataListener: LoadDataListener) {
    var listMusic = ArrayList<Music>()

    fun getMusicData() {
        var resolver: ContentResolver? = context?.contentResolver
        var query =
            resolver?.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null
            )
                ?: throw IllegalStateException("Unable to query ${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI}, system returned null.")
        query?.use { cursor ->
            while (cursor.moveToNext()) {
                var name = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                var path = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                var duration = cursor?.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val formmatTime = SimpleDateFormat("mm:ss")
                var timer = formmatTime.format(duration)
                Log.d("List Music", "$name,${formmatTime.format(duration)},$path ")
                var music = Music(name!!, path!!, timer)
                listMusic.add(music)
            }
            loadDataListener.onLoadData(listMusic)
        }
    }
}