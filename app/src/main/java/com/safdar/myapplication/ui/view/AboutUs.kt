package com.safdar.myapplication.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.AboutUsBinding


class AboutUs:AppCompatActivity() {
    lateinit var binding: AboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.about_us)
        supportActionBar?.hide()
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}