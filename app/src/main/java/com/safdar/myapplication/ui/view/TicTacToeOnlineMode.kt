package com.safdar.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.TicTacToeOnlineModeBinding
import com.safdar.myapplication.ui.utils.GameFunction

class TicTacToeOnlineMode : AppCompatActivity() {
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
        binding.ticTacTaeOnlinePlayButton.setOnClickListener {
            code = "null"
            keyValue = "null"
            code = GameFunction.getRandomString(10)
            Log.i("SafdarKhan", "Generated Code $code")
            if (code != "null" && code != "") {
                var codeGenerated = true
                isCodeMaker = true
                binding.idBtnProgressBar.visibility = View.VISIBLE
                Handler().postDelayed({
                    FirebaseDatabase.getInstance().reference.child("codes")
                        .addValueEventListener(object :
                            ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(codeGenerated) {
                                    codeGenerated = false
                                    FirebaseDatabase.getInstance().reference.child("codes").push()
                                        .setValue(code)
                                    Handler().postDelayed({
                                        accepted()
                                        binding.idBtnProgressBar.visibility = View.GONE
                                        Toast.makeText(
                                            this@TicTacToeOnlineMode,
                                            "Please Don,t Go back",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }, 300)


                                }
                            }
                        })
                },2000)
            }else{
                Toast.makeText(this@TicTacToeOnlineMode, "Please Try Again", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ticTacTaeOnlineJoinButton.setOnClickListener {
            startActivity(Intent(this@TicTacToeOnlineMode,OnlineCodeGenerator::class.java))
        }
    }
    private fun accepted() {
        startActivity(Intent(this@TicTacToeOnlineMode, OnlineGamepage::class.java))
    }
}