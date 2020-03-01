package com.example.tochkaapp.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.example.tochkaapp.R


/**
 * Created by Vladimir Kraev
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
}