package com.yaseminuctas.arabamisat.extension

import android.widget.ImageView
import com.google.firebase.storage.StorageReference
import com.yaseminuctas.arabamisat.GlideApp

fun ImageView.loadImage(storageRef: StorageReference){
    GlideApp.with(this.context.applicationContext).load(storageRef).into(this)
}
