package com.example.lab4.ui.extensions

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.Toast

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.toastLong(message: String) {
    toastMessageDuration(message, Toast.LENGTH_LONG)
}

fun Context.toastMessageDuration(message: String, durationToast: Int) {
    val mainHandler = Handler(this.mainLooper)
    val runnable = Runnable {
        Toast.makeText(this, message, durationToast).show()
    }
    mainHandler.post(runnable)
}