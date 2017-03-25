package com.bachhuberdesign.gwentcardviewer

import android.app.Application

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class App : Application() {

    companion object {
        @JvmStatic val TAG = this::class.java.name
    }

}
