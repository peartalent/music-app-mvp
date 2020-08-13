package com.dinhtai.musicapp.view.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dinhtai.musicapp.R
import com.dinhtai.musicapp.model.entity.Music
import com.dinhtai.musicapp.presenter.MainPresenter
import com.dinhtai.musicapp.service.ServiceMusic
import com.dinhtai.musicapp.view.adapter.AdapterMusic
import com.dinhtai.musicapp.view.adapter.MainViewListener
import com.dinhtai.musicapp.view.adapter.OnClickItemListener
import com.dinhtai.musicapp.view.dialog.CustomDialogListMusic
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity(), OnClickItemListener, View.OnClickListener {
    private var mainPresenter: MainPresenter? = null
    private var adapterMusic: AdapterMusic? = null
    private var recyclerView: RecyclerView? = null
    private var customDialog: CustomDialogListMusic? = null
    private var musics: MutableList<Music> = mutableListOf()
    private lateinit var serviceMusic: ServiceMusic
    private var isConnect = false
    private val connectServiceMusic = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnect = false
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val myBinder = p1 as ServiceMusic.MyBinder
            serviceMusic = myBinder.getMusicControl()
            isConnect = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
        assignViews(img_play, img_show_list, img_next, img_background, img_previous, img_stop)
        Intent(this, ServiceMusic::class.java).also { intent ->
            bindService(
                intent,
                connectServiceMusic,
                Context.BIND_AUTO_CREATE
            )
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                serviceMusic.updateSeekToMediaPlayer(seek_bar.progress)

            }

        })
    }

    override fun onClickItem(position: Int) {
        serviceMusic.stopMediaPlayer()
        serviceMusic.createMediaPlayer(position)
        updateNameSong()
        totalTime()
        img_play.setImageResource(R.drawable.ic_pause_24)
        updateTimeSong()
        customDialog?.dismiss()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_play -> {

                if (serviceMusic.getMediaPlayer() != null) {
                    serviceMusic.resumeMediaPlayer()
                    if (serviceMusic.getMediaPlayer()?.isPlaying!!) {
                        img_play.setImageResource(R.drawable.ic_pause_24)
                    } else {
                        img_play.setImageResource(R.drawable.ic_play_24)
                    }
                } else {
                    serviceMusic.startMediaPlayer()
                    img_play.setImageResource(R.drawable.ic_pause_24)
                }
                totalTime()
                updateNameSong()
                updateTimeSong()
            }
            R.id.img_next -> {
                serviceMusic.nextMediaPlayer()
                updateNameSong()
                totalTime()
                img_play.setImageResource(R.drawable.ic_pause_24)
                updateTimeSong()
            }
            R.id.img_previous -> {
                serviceMusic.previousMediaPlayer()
                updateNameSong()
                totalTime()
                img_play.setImageResource(R.drawable.ic_pause_24)
                updateTimeSong()
            }
            R.id.img_stop -> {
                serviceMusic.stopMediaPlayer()
                img_play.setImageResource(R.drawable.ic_play_24)
                seek_bar.setProgress(0)
                txt_time_start_song.text = "00:00"
            }
            R.id.img_show_list -> {
                musics.clear()
                musics.addAll(serviceMusic.getListMusic()!!)
                adapterMusic = musics?.let { AdapterMusic(it, this) }
                adapterMusic?.let { customDialog = CustomDialogListMusic(this, it) }
                customDialog!!.show()
            }


        }
    }

    fun updateNameSong() {
        serviceMusic.getMediaPlayer()?.let {
            txt_name_music.text = serviceMusic.getNameSong()
            seek_bar.max = serviceMusic.getDurationMediaPlayer()!!
            seek_bar.max = serviceMusic.getDurationMediaPlayer()!!
        }
    }

    fun updateTimeSong() {
        var handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                var formatTime = SimpleDateFormat("mm:ss")
                serviceMusic.getMediaPlayer()?.let {
                    txt_time_start_song.text = formatTime.format(serviceMusic.getCurrentPositionMediaPlayer())
                    seek_bar.setProgress(serviceMusic.getCurrentPositionMediaPlayer()!!)
                    serviceMusic.getMediaPlayer()?.setOnCompletionListener {
                        serviceMusic.nextMediaPlayer()
                        img_play.setImageResource(R.drawable.ic_pause_24)
                        totalTime()
                        updateNameSong()
                    }
                }
                handler.postDelayed(this, 100)
            }

        }, 10)

    }

    fun totalTime() {
        var format = SimpleDateFormat("mm:ss")
        serviceMusic.getDurationMediaPlayer()?.let {
            txt_time_total.text = format.format(it)
        }

    }

    override fun onDestroy() {
        if (isConnect) {
            unbindService(connectServiceMusic)
            isConnect = false
        }
        super.onDestroy()
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Quyền được chấp nhận", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Quyền không được cấp, ứng dụng sẽ đóng", Toast.LENGTH_SHORT)
                        .show()
                }
                //Register permission
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
        when (requestCode) {
            1 -> {
                if (grantResults.size == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    fun View.OnClickListener.assignViews(vararg views: View?) {
        views.forEach { it?.setOnClickListener(this) }
    }

}

