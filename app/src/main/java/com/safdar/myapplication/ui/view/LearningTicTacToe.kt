package com.safdar.myapplication.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.LearningTicTacToeBinding

class LearningTicTacToe :AppCompatActivity(){
    private lateinit var binding:LearningTicTacToeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.learning_tic_tac_toe)
        handleOnClickListener()
        hideActionBar()
    }
    private fun hideActionBar() {
        supportActionBar?.hide();
    }

    private fun handleOnClickListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}