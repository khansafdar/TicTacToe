package com.safdar.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.OnlineCodeGeneratorBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var isCodeMaker = true;
var code = "null";
var codeEntered= "null"
var keyValue: String = "null"

class OnlineCodeGenerator : AppCompatActivity() {
    lateinit var binding: OnlineCodeGeneratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.online_code_generator)
        setOnClickListener()
        hideActionBar()
    }
    private fun hideActionBar() {
        supportActionBar?.hide()
    }


    private fun setOnClickListener() {
        binding.idBtnJoinGame.setOnClickListener {
            codeEntered = "null"
            keyValue = "null"
            codeEntered = binding.idEtInputCode.text.toString()
            Log.i("SafdarKhan", "Entered Code:- $codeEntered ")
            if (codeEntered != "null" && code != "") {
                invisiblePage()
                enableProgressbar()
                var codeGenerated = false
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!codeGenerated) {
                            codeGenerated = true
                            var data: Boolean = isValueAvailable(snapshot, codeEntered)
                            Handler().postDelayed({
                                if (data) {
                                    accepted()
                                } else {
                                    Toast.makeText(
                                        this@OnlineCodeGenerator,
                                        "Please enter the Correct Code To Join",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    visiblePage()
                                    disableProgressbar()
                                }
                            }, 1000)
                        }
                    }
                })
            } else {
                Toast.makeText(this@OnlineCodeGenerator, "Please enter the Correct Code To Join", Toast.LENGTH_SHORT).show()
                visiblePage()
                disableProgressbar()
            }
        }
    }

    private fun accepted() {
        startActivity(Intent(this@OnlineCodeGenerator, OnlineGamepage::class.java))
    }
    private fun invisiblePage(){
        binding.idEtInputCode.visibility = View.INVISIBLE
        binding.idBtnJoinGame.visibility = View.INVISIBLE
    }
    private fun visiblePage(){
        binding.idEtInputCode.visibility = View.VISIBLE
        binding.idBtnJoinGame.visibility = View.VISIBLE
    }
    private fun enableProgressbar(){
        binding.idBtnProgressBar.visibility = View.VISIBLE
    }
    private fun disableProgressbar(){
        binding.idBtnProgressBar.visibility = View.INVISIBLE
    }

    private fun isValueAvailable(snapshot: DataSnapshot, codeEntered: String): Boolean {
        var data = snapshot.children
        data.forEach {
            var value = it.value.toString()
            if (value == codeEntered) {
                code = codeEntered
                keyValue = it.key.toString()
                return true
            }
        }
        return false
    }
}