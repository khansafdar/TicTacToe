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
        binding.ticTacTaePlayButton.setOnClickListener {
            startActivity(Intent(this@TicTacToe, GamePage::class.java))
        }
        binding.ticTacToeInstruction.setOnClickListener {
            startActivity(Intent(this@TicTacToe, LearningTicTacToe::class.java))
        }
        binding.ticTacToeAboutUs.setOnClickListener {
            startActivity(Intent(this@TicTacToe,AboutUs::class.java))
        }
    }
}