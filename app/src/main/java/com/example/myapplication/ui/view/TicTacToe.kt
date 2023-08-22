package com.example.myapplication.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.TicTacToeBinding

class TicTacToe :Activity(){
    lateinit var binding:TicTacToeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.tic_tac_toe)
        initClickListener()
    }


    private fun initClickListener() {
        binding.ticTacTaePlayButton.setOnClickListener {
            startActivity(Intent(this@TicTacToe, GamePage::class.java))
        }
    }
}