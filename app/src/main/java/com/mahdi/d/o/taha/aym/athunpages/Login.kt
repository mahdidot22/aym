package com.mahdi.d.o.taha.aym.athunpages

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.mahdi.d.o.taha.aym.R
import com.mahdi.d.o.taha.aym.Socket_Create
import com.mahdi.d.o.taha.aym.Users
import com.mahdi.d.o.taha.aym.constant.Conostants
import com.mahdi.d.o.taha.aym.models.User_model
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.root
import org.json.JSONArray
import org.json.JSONObject

class Login : AppCompatActivity() {
    lateinit var socketCreate: Socket_Create
    private var mSocket: Socket? = null
    var conostants = Conostants()
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        socketCreate = application as Socket_Create
        mSocket = socketCreate.getSocket()
        mAuth = FirebaseAuth.getInstance()
        val data = intent.getBundleExtra("data")
        var name = data?.getString("email")
        var username = ed_login_email
        if (name == null) {
            name = ""
        }
        username.setText(name)
        var psw = ed_login_password

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

        btn_login.setOnClickListener {
            if (username.text.toString().isNotEmpty() && psw.text.toString().isNotEmpty()) {
                login(username, psw, mSocket!!, this, mAuth!!)
            } else {
                conostants.Snackbar(root, "Fill Fields!!")
            }
        }
        mSocket!!.on("onlineUsers", onNewUser)
        mSocket!!.connect()
        tv_toSignup.setOnClickListener {
            conostants.start_Activity(this, Signup::class.java)
            finish()
        }
    }

    val onNewUser = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val users = args[0] as JSONArray
                val list = ArrayList<User_model>()
                Log.e("mdot", users.toString())
                for (i in 0 until users.length()) {
                    var user = users.getJSONObject(i)
                    var id = user.getString("user_id")
                    var username = user.getString("user_name")
                    list.add(User_model(id, username, "Active"))
                }
                val sharedpreferences =
                    getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedpreferences.edit()
                val gson = Gson()
                val json = gson.toJson(list)
                editor.putString("list", json)
                editor.apply()
            } catch (e: Exception) {
                Log.e("mdot", e.toString())
            }
        }
    }

    private fun login(
        username: EditText,
        psw: EditText,
        mSocket: Socket,
        activity: Activity,
        mAuth: FirebaseAuth
    ) {
        var current_id = ""
        mAuth!!.signInWithEmailAndPassword(username.text.toString(), psw.text.toString())
            .addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    current_id = mAuth!!.currentUser!!.uid
                    userOnline(current_id, username.text.toString(), mSocket)
                    username.text.clear()
                    psw.text.clear()
                } else {
                    conostants.Snackbar(root, "User dose not exists before!!")
                    psw.text.clear()
                }
            }
    }

    private fun userOnline(user_id: String, username: String, mSocket: Socket?) {
        var userObj = JSONObject()
        userObj.put("user_id", user_id)
        userObj.put("user_name", username)
        mSocket!!.emit("userObj", userObj)
        val b = Bundle()
        b.putString("current_id", user_id)
        b.putString("user_name", username)
        conostants.start_Activity(this, Users::class.java, b)
        finish()
    }
}
