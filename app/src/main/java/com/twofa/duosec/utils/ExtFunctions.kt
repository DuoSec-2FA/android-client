package com.twofa.duosec.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.hideActionBar() {
    if (supportActionBar != null) {
        supportActionBar!!.hide()
    }
}


fun Context.toast(message: String, isLong: Boolean = false) {
    if (isLong) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
