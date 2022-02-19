package com.mahdi.d.o.taha.aym.constant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View

class Conostants {
    lateinit var intent: Intent
    fun start_Activity(context: Context, cls: Class<*>) {
        intent = Intent(context, cls)
        context.startActivity(intent)
    }

    fun start_Activity(context: Context, cls: Class<*>, data: Bundle) {
        intent = Intent(context, cls)
        intent.putExtra("data", data)
        context.startActivity(intent)
    }

    fun Snackbar(root: View, text: String) {
        var snackbar = com.google.android.material.snackbar.Snackbar.make(
            root,
            text,
            com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Dismiss") {
            it.setOnClickListener {
                snackbar.dismiss()
            }
        }.show()
    }
}
