package com.example.myapplication.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.TicTacToeBinding

class TicTacToe :AppCompatActivity(){
    lateinit var binding:TicTacToeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.tic_tac_toe)
        initClickListener()
        hideActionBar()
    }

    private fun hideActionBar() {
        supportActionBar?.hide();
    }

    private fun initClickListener() {
        binding.ticTacTaeOfflineButton.setOnClickListener {
            startActivity(Intent(this@TicTacToe, TicTacToeOfflineMode::class.java))
        }
        binding.ticTacTaeOnlineButton.setOnClickListener {
            startActivity(Intent(this@TicTacToe, TicTacToeOnlineMode::class.java))
        }


    }
}