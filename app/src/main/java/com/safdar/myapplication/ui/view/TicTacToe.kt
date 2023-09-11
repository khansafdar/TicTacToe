package com.safdar.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.TicTacToeBinding

class TicTacToe :AppCompatActivity(){
    lateinit var binding:TicTacToeBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
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
        binding.ticTacTaeLearnButton.setOnClickListener {
            startActivity(Intent(this@TicTacToe, LearningTicTacToe::class.java))
        }
        binding.ticTacTaeAboutButton.setOnClickListener {
            startActivity(Intent(this@TicTacToe, AboutUs::class.java))
        }
        binding.logout.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this@TicTacToe,LoginPage::class.java))
        }

    }
}