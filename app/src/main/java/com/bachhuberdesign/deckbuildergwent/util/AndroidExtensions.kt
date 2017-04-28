package com.bachhuberdesign.deckbuildergwent.util

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/*
 * A collection of helpful functions that extend base Android framework classes such as View.
 *
 * Non-Android extension functions should be in a separate file.
 *
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Cursor.getBooleanFromColumn(columnName: String): Boolean {
    val columnValue = getInt(getColumnIndex(columnName))

    when (columnValue) {
        0 -> return false
        1 -> return true
        else -> throw IndexOutOfBoundsException("$columnName value was $columnValue, expected 0 or 1.")
    }
}

fun Cursor.getIntFromColumn(columnName: String): Int {
    return getInt(getColumnIndex(columnName))
}

fun Cursor.getLongFromColumn(columnName: String): Long {
    return getLong(getColumnIndex(columnName))
}

fun Cursor.getStringFromColumn(columnName: String): String {
    return getString(getColumnIndex(columnName))
}

fun Cursor.getDoubleFromColumn(columnName: String): Double {
    return getDouble(getColumnIndex(columnName))
}

fun Cursor.getFloatFromColumn(columnName: String): Float {
    return getFloat(getColumnIndex(columnName))
}

fun Cursor.getBlobFromColumn(columnName: String): ByteArray {
    return getBlob(getColumnIndex(columnName))
}

fun Context.getStringResourceByName(key: String): String {
    val resourceId = this.resources.getIdentifier(key, "string", this.packageName)
    return this.getString(resourceId)
}

fun Context.dpToPx(dp: Int): Float {
    val scale = this.resources.displayMetrics.density
    return dp * scale + 0.5f
}