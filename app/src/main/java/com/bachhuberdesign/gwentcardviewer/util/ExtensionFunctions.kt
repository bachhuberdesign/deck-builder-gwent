package com.bachhuberdesign.gwentcardviewer.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}