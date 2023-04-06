package com.nearby.task.utils
import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast




fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

interface DialogCallbackCameraGallery {
    fun onYes(from: String)
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


