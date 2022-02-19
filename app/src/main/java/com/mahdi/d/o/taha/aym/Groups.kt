package com.mahdi.d.o.taha.aym

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.mahdi.d.o.taha.aym.adapters.GroupRecyclerAdapter
import com.mahdi.d.o.taha.aym.constant.Conostants
import com.mahdi.d.o.taha.aym.models.Group_model
import kotlinx.android.synthetic.main.activity_groups.*
import kotlinx.android.synthetic.main.group_item.view.*
import org.json.JSONArray

class Groups : AppCompatActivity(), GroupRecyclerAdapter.onClick {
    lateinit var data: Bundle
    lateinit var socketCreate: Socket_Create
    private var mSocket: Socket? = null
    var conostants = Conostants()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)
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
                Log.e("onDisconnect", "Socket onDisconnect!")
                conostants.Snackbar(root, "SERVER_CONNECT_ERROR")
            }
        }
        mSocket!!.on("groups", onNewGroup)
        mSocket!!.connect()

        btn_create_group.setOnClickListener {
            createGroup(mSocket!!)
        }
    }

    override fun onItemClick(position: Int) {
        group_recycler[position].join.setOnClickListener {
            data.putString("public", "public")
            data.putString("src_id", data.getString("src_id"))
            data.putString("des_id", data.getString("des_id"))
            data.putString("user_name", data.getString("user_name"))
            conostants.start_Activity(this, Msg::class.java, data)
        }
    }

    fun createGroup(mSocket: Socket) {
        val name = "Public Room"
        mSocket!!.emit("newgroup", name)
    }

    val onNewGroup = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val groups = args[0] as JSONArray
                var list = ArrayList<Group_model>()
                for (i in 0 until groups.length()) {
                    var name = groups.getString(i)
                    list.add(Group_model(name))
                }
                val adapter = GroupRecyclerAdapter(this, list, this)
                group_recycler.layoutManager = LinearLayoutManager(this)
                group_recycler.adapter = adapter
                btn_refresh.setOnClickListener {
                    val adapter = GroupRecyclerAdapter(this, list, this)
                    group_recycler.layoutManager = LinearLayoutManager(this)
                    group_recycler.adapter = adapter
                }
            } catch (e: Exception) {
                Log.e("mdot", e.toString())
            }
        }
    }
}
