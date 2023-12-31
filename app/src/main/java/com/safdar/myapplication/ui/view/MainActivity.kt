package com.safdar.myapplication.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.ActivityMainBinding

class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        splashScreenTime()
    }



    private fun splashScreenTime() {
        val timer1: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@MainActivity, LoginPage::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        timer1.start()
    }
}