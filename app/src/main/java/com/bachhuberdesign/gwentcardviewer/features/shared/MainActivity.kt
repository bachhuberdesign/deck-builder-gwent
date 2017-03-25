package com.bachhuberdesign.gwentcardviewer.features.shared

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bachhuberdesign.gwentcardviewer.R

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic val TAG = this::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
