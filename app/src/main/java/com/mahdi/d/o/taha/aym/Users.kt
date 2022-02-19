package com.mahdi.d.o.taha.aym

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahdi.d.o.taha.aym.adapters.UserRecyclerAdapter
import com.mahdi.d.o.taha.aym.constant.Conostants
import com.mahdi.d.o.taha.aym.models.User_model
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.user_item.view.*
import java.lang.reflect.Type

class Users : AppCompatActivity(), UserRecyclerAdapter.onClick {
    lateinit var data: Bundle
    var conostants = Conostants()
    lateinit var socketCreate: Socket_Create
    private var mSocket: Socket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        socketCreate = application as Socket_Create
        mSocket = socketCreate.getSocket()
        data = intent.getBundleExtra("data")!!
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR) {
            runOnUiThread {
                Log.e("EVENT_CONNECT_ERROR", "EVENT_CONNECT_ERROR: ")
                conostants.Snackbar(root, "SERVER_CONNECT_ERROR")
            }
        }

        mSocket!!.on(Socket.EVENT_DISCONNECT) {
            runOnUiThread {
                conostants.Snackbar(root, "SERVER_CONNECT_ERROR")
            }
        }
        mSocket!!.connect()
        val sharedpreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedpreferences.getString("list", "")
        if (json!!.isNotEmpty()) {
            val type: Type = object : TypeToken<ArrayList<User_model>>() {}.type
            val list: ArrayList<User_model> = gson.fromJson(json, type)
            val adapter = UserRecyclerAdapter(this, list, this)
            users_recycler.layoutManager = LinearLayoutManager(this)
            users_recycler.adapter = adapter
        }
        btn_refresh.setOnClickListener {
            val sharedpreferences =
                getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedpreferences.getString("list", "")
            if (json!!.isNotEmpty()) {
                val type: Type = object : TypeToken<ArrayList<User_model>>() {}.type
                val list: ArrayList<User_model> = gson.fromJson(json, type)
                val adapter = UserRecyclerAdapter(this, list, this)
                users_recycler.layoutManager = LinearLayoutManager(this)
                users_recycler.adapter = adapter
            }
        }
    }

    override fun onItemClick(position: Int) {
        var current_id = data.getString("current_id")
        var sender = data.getString("user_name")
        Log.e("mdotid", current_id.toString())
        val b = Bundle()
        var des_id: String
        users_recycler[position].join.setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.popupmenu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                des_id = users_recycler[position].user_id.text.toString()
                when (item.itemId) {
                    R.id.Chat -> {
                        b.putString("des_id", des_id)
                        b.putString("src_id", current_id)
                        b.putString("user_name", sender)
                        conostants.start_Activity(this, Msg::class.java, b)
                    }
                    R.id.Group -> {
                        b.putString("des_id", des_id)
                        b.putString("src_id", current_id)
                        b.putString("user_name", sender)
                        conostants.start_Activity(this, Groups::class.java, b)
                    }
                }
                true
            }
            popup.show()
        }
    }
}
