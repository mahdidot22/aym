package com.mahdi.d.o.taha.aym

import android.app.Application
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

class Socket_Create : Application() {

    private var mSocket: Socket? = IO.socket("http://192.168.1.93:3000")

    fun getSocket(): Socket? {
        return mSocket
    }
}
