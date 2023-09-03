package com.safdar.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.TicTacToeBinding
import com.example.myapplication.databinding.TicTacToeOnlineModeBinding

class TicTacToeOnlineMode:AppCompatActivity() {
    lateinit var binding: TicTacToeOnlineModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.tic_tac_toe_online_mode)
        initClickListener()
        hideActionBar()
    }
    private fun hideActionBar() {
        supportActionBar?.hide();
    }

    private fun initClickListener() {
        binding.ticTacTaeOnlineButton.setOnClickListener {
            startActivity(Intent(this@TicTacToeOnlineMode, OnlineCodeGenerator::class.java))
        }
    }
}