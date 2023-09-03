package com.safdar.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.OnlineCodeGeneratorBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var isCodeMaker = true;
var code = "null";
var codeFound = false
var checkTemp = true
var keyValue: String = "null"

class OnlineCodeGenerator : AppCompatActivity() {
    lateinit var binding: OnlineCodeGeneratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.online_code_generator)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.idBtnCodeGenerate.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = binding.idEtInputCode.text.toString()
            if (code != "null" && code != "") {
                isCodeMaker = true
                invisiblePage()
                enableProgressbar()
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var check: Boolean = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if (check) {
                                Toast.makeText(this@OnlineCodeGenerator, "Please enter Different Code To Join", Toast.LENGTH_SHORT).show()
                                visiblePage()
                                disableProgressbar()
                            } else {
                                FirebaseDatabase.getInstance().reference.child("codes").push().setValue(
                                    code
                                )
                                isValueAvailable(snapshot, code)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(this@OnlineCodeGenerator, "Please Don,t Go back", Toast.LENGTH_SHORT).show()
                                }, 300)
                            }
                        }, 1000)
                    }
                })
            } else {
                Toast.makeText(this@OnlineCodeGenerator, "Please enter the Correct Code To Join", Toast.LENGTH_SHORT).show()
                disableProgressbar()
                visiblePage()
            }

        }
        binding.idBtnJoinGame.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = binding.idEtInputCode.text.toString()
            if (code != "null" && code != "") {
                invisiblePage()
                enableProgressbar()
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data: Boolean = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if (data) {
                                codeFound = true
                                accepted()
                            } else {
                                Toast.makeText(this@OnlineCodeGenerator, "Please enter the Correct Code To Join", Toast.LENGTH_SHORT).show()
                                visiblePage()
                                disableProgressbar()
                            }
                        }, 1000)
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
        binding.idBtnCodeGenerate.visibility = View.INVISIBLE
        binding.idBtnJoinGame.visibility = View.INVISIBLE
    }
    private fun visiblePage(){
        binding.idEtInputCode.visibility = View.VISIBLE
        binding.idBtnCodeGenerate.visibility = View.VISIBLE
        binding.idBtnJoinGame.visibility = View.VISIBLE
    }
    private fun enableProgressbar(){
        binding.idBtnProgressBar.visibility = View.VISIBLE
    }
    private fun disableProgressbar(){
        binding.idBtnProgressBar.visibility = View.INVISIBLE
    }

    private fun isValueAvailable(snapshot: DataSnapshot, code: String): Boolean {
        var data = snapshot.children
        data.forEach {
            var value = it.value.toString()
            if (value == code) {
                keyValue = it.key.toString()
                return true
            }
        }
        return false
    }
}