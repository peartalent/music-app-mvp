package com.dinhtai.musicapp.view.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinhtai.musicapp.R
import com.dinhtai.musicapp.model.entity.Music
import com.dinhtai.musicapp.view.activity.MainActivity
import com.dinhtai.musicapp.view.adapter.AdapterMusic
import kotlinx.android.synthetic.main.list_item.*
class CustomDialogListMusic(var activity: Activity, internal var adapter: RecyclerView.Adapter<*>) : Dialog(activity) {
    var dialog: Dialog? = null

    internal var recyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.list_item)
        recyclerView = rv_list_item
        mLayoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = adapter

    }

}