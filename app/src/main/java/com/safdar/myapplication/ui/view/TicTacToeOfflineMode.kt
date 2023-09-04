package com.safdar.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.TicTacToeOfflineModeBinding

class TicTacToeOfflineMode:AppCompatActivity() {
    lateinit var binding: TicTacToeOfflineModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.tic_tac_toe_offline_mode)
        initClickListener()
        hideActionBar()
    }
    private fun hideActionBar() {
        supportActionBar?.hide();
    }

    private fun initClickListener() {
        binding.ticTacTaeBotButton.setOnClickListener {
            startActivity(Intent(this@TicTacToeOfflineMode, Bot::class.java))
        }
        binding.ticTacTaeFriendButton.setOnClickListener {
            startActivity(Intent(this@TicTacToeOfflineMode, GamePage::class.java))
        }


    }
}