package com.mahdi.d.o.taha.aym

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.mahdi.d.o.taha.aym.adapters.ReceiveMessageItem
import com.mahdi.d.o.taha.aym.adapters.SendMessageItem
import com.mahdi.d.o.taha.aym.constant.Conostants
import com.mahdi.d.o.taha.aym.models.Msg_model
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_msg.*
import org.json.JSONObject
import java.lang.Exception

class Msg : AppCompatActivity() {
    lateinit var socketCreate: Socket_Create
    private var mSocket: Socket? = null
    lateinit var data: Bundle
    lateinit var _user_id: String
    lateinit var _des_id: String
    lateinit var sender: String
    val conostants = Conostants()
    private val messageAdapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg)
        data = intent.getBundleExtra("data")!!
        _user_id = data.getString("src_id")!!
        _des_id = data.getString("des_id")!!
        sender = data.getString("user_name")!!
        socketCreate = application as Socket_Create
        mSocket = socketCreate.getSocket()

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

        mSocket!!.on("msgObj", OnNewMessage)
        mSocket!!.connect()

        _msg_send.setOnClickListener {
            if (_msg_text.text.isNotEmpty()) {
                sendMessage()
            }
        }

        _msg_list.adapter = messageAdapter
        populateData()
    }

    val OnNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val massage = args[0] as JSONObject
                if (_user_id == massage.getString("des_id")) {
                    var msg = massage.getString("text_msg")
                    var sender = massage.getString("sender_name")
                    val receive =
                        Msg_model(
                            msg = msg,
                            sendBy = massage.getString("src_id"),
                            name = sender
                        )
                    val receiveItem = ReceiveMessageItem(receive)
                    messageAdapter.add(receiveItem)
                } else {
                    if (data.getString("public") == "public") {
                        var msg = massage.getString("text_msg")
                        var sender = massage.getString("sender_name")
                        val receive =
                            Msg_model(
                                msg = msg,
                                sendBy = massage.getString("src_id"),
                                name = sender
                            )
                        val receiveItem = ReceiveMessageItem(receive)
                        messageAdapter.add(receiveItem)
                    }
                }
            } catch (e: Exception) {
                Log.e("mdot", e.toString())
            }
        }
    }

    private fun sendMessage() {
        val message =
            Msg_model(msg = _msg_text.text.toString(), sendBy = _user_id, sender.substring(0, 6))
        val sendMessageItem = SendMessageItem(message)
        messageAdapter.add(sendMessageItem)
        var msgObj = JSONObject()
        msgObj.put("text_msg", _msg_text.text.toString())
        msgObj.put("src_id", _user_id)
        msgObj.put("des_id", _des_id)
        msgObj.put("sender_name", sender.substring(0, 6))
        mSocket!!.emit("msgObj", msgObj)
        _msg_text.text.clear()
    }

    private fun populateData() {
        val data = listOf<Msg_model>()
        data.forEach {
            if (it.sendBy == _user_id) {
                messageAdapter.add(SendMessageItem(it))
            } else {
                messageAdapter.add(ReceiveMessageItem(it))
            }
        }
    }
}
