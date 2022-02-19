package com.mahdi.d.o.taha.aym.athunpages

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mahdi.d.o.taha.aym.R
import com.mahdi.d.o.taha.aym.constant.Conostants
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.root

class Signup : AppCompatActivity() {
    private var conostants = Conostants()
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mAuth = FirebaseAuth.getInstance()
        var username = ed_signup_email
        var psw = ed_signup_password
        var confirmpsw = ed_signup_confirm
        btn_signup.setOnClickListener {
            addUser(username, psw, confirmpsw, this, mAuth!!)
        }
        tv_toLogin.setOnClickListener {
            conostants.start_Activity(this, Login::class.java)
            finish()
        }
    }

    private fun addUser(
        username: EditText,
        psw: EditText,
        confirmpsw: EditText,
        activity: Activity,
        mAuth: FirebaseAuth
    ) {
        if (username.text.isNotEmpty() &&
            psw.text.isNotEmpty() &&
            confirmpsw.text.isNotEmpty()
        ) {
            athun(psw, confirmpsw, username, activity, mAuth)
        } else {
            conostants.Snackbar(root, "Fill Fields!!")
        }
    }

    private fun athun(
        psw: EditText,
        confirm_psw: EditText,
        username: EditText,
        activity: Activity,
        mAuth: FirebaseAuth
    ) {

        if (psw.text.toString() == confirm_psw.text.toString()) {
            if (psw.text.toString().length < 8 || confirm_psw.text.toString().length < 8) {
                conostants.Snackbar(
                    root,
                    "Unacceptable password length!! Must be more than 8 characters."
                )
            } else {
                mAuth!!.createUserWithEmailAndPassword(
                    username.text.toString(),
                    psw.text.toString()
                )
                    .addOnCompleteListener(
                        activity
                    ) { task ->
                        if (task.isSuccessful) {
                            val user_email = mAuth!!.currentUser!!.email
                            val b = Bundle()
                            b.putString("email", user_email)
                            conostants.start_Activity(activity, Login::class.java, b)
                            conostants.Snackbar(root, "User added successfully!!")
                            username.text.clear()
                            psw.text.clear()
                        } else {
                            conostants.Snackbar(root, "User added failed!!!!")
                            psw.text.clear()
                        }
                    }
            }
        } else {
            conostants.Snackbar(root, "Password does not match!!")
        }
    }
}
