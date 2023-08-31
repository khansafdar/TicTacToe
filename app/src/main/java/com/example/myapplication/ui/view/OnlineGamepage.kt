package com.example.myapplication.ui.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.databinding.OnlineGamePageBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.Arrays

class OnlineGamepage:AppCompatActivity() {
    val view = arrayListOf<View>()
    var isMyMove = MutableLiveData(isCodeMaker)
    private lateinit var binding: OnlineGamePageBinding
    var gameActive = true

    // Player representation
    // 0 - X
    // 1 - O
    private var activePlayer = 0
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)

    // State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null
    // put all win positions in a 2D array
    var winPositions = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
    )
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.online_game_page)
        addView()
        observer()
        handleOnClickListener()
        hideActionBar()
        gameReset()
        FirebaseDatabase.getInstance().reference.child("data").addChildEventListener(object:
                ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var data = snapshot.value
                if(isMyMove.value==true){
                    isMyMove.value = false
                }else{
                    isMyMove.value = true
                    playerTap(view[data.toString().toInt()])
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun addView() {
        view.add(binding.imageView0)
        view.add(binding.imageView1)
        view.add(binding.imageView2)
        view.add(binding.imageView3)
        view.add(binding.imageView4)
        view.add(binding.imageView5)
        view.add(binding.imageView6)
        view.add(binding.imageView7)
        view.add(binding.imageView8)
    }

    private fun observer() {
        isMyMove.observe(this){
            if(isMyMove.value==true){
                binding.status.text = "Your turn"
            }else{
                binding.status.text = "Other turn"
            }
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun handleOnClickListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun DisbleClick(){
        binding.imageView0.isClickable  =  false
        binding.imageView1.isClickable  =  false
        binding.imageView2.isClickable  =  false
        binding.imageView3.isClickable  =  false
        binding.imageView4.isClickable  =  false
        binding.imageView5.isClickable  =  false
        binding.imageView6.isClickable  =  false
        binding.imageView7.isClickable  =  false
        binding.imageView8.isClickable  =  false
    }
    private fun EnableClick(){
        binding.imageView0.isClickable  =  true
        binding.imageView1.isClickable  =  true
        binding.imageView2.isClickable  =  true
        binding.imageView3.isClickable  =  true
        binding.imageView4.isClickable  =  true
        binding.imageView5.isClickable  =  true
        binding.imageView6.isClickable  =  true
        binding.imageView7.isClickable  =  true
        binding.imageView8.isClickable  =  true
    }
    private fun StartButton(){
        binding.startButton.setOnClickListener {
            EnableClick()
            gameReset()
        }
    }

    private fun EnableTheClick() {
        binding.imageView0.setOnClickListener{
            binding.imageView0.isClickable = true
        }
        gameReset()
    }



    fun playerTap(view: View) {
        binding.startButton.text = "Reset Game"
        val img = view as ImageView
        val tappedImage = img.tag.toString().toInt()

        // game reset function will be called
        // if someone wins or the boxes are full

        // if the tapped image is empty
        if (gameState[tappedImage] === 2) {
            // increase the counter
            // after every tap
            counter++

            // check if its the last box
            if (counter == 9) {
                // reset the game
                gameActive = false
            }

            // mark this position
            gameState[tappedImage] = activePlayer

            // this will give a motion
            // effect to the image
            img.translationY = -1000f

            // change the active player
            // from 0 to 1 or 1 to 0
            if (activePlayer == 0) {
                // set the image of x
                img.setImageResource(R.drawable.x)
                activePlayer = 1

                // change the status
            } else {
                // set the image of o
                img.setImageResource(R.drawable.o)
                activePlayer = 0
                val status = findViewById<TextView>(R.id.status)

                // change the status
            }
            img.animate().translationYBy(1000f).duration = 300
        }
        var flag = 0
        // Check if any player has won if counter is > 4 as min 5 taps are
        // required to declare a winner
        if (counter > 4) {
            for (winPosition in winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] && gameState[winPosition[0]] != 2) {
                    flag = 1

                    // Somebody has won! - Find out who!
                    drawLine(winPosition)

                    // game reset function be called
                    gameActive = false
                    val winnerStr: String = if (gameState[winPosition[0]] === 0) {
                        DisbleClick()
                        binding.startButton.text = "Reset Game"
                        "X has won"
                    } else {
                        DisbleClick()
                        binding.startButton.text = "Reset Game"
                        "O has won"
                    }
                    // Update the status bar for winner announcement
                    binding.status.text = winnerStr
                }
            }
            // set the status if the match draw
            if (counter == 9 && flag == 0) {
                DisbleClick()
                binding.startButton.text = "Reset Game"
                binding.status.text = "Match Draw"
            }
        }
    }

    private fun drawLine(WinPosition:IntArray) {
        if(WinPosition[0]==0 && WinPosition[1]==1 && WinPosition[2]==2){
            binding.row1.apply {
                setImageResource(R.drawable.horizontal)
            }
        }else if(WinPosition[0]==3 && WinPosition[1]==4 && WinPosition[2]==5){
            binding.row2.apply {
                setImageResource(R.drawable.horizontal)
            }
        }else if(WinPosition[0]==6 && WinPosition[1]==7 && WinPosition[2]==8){
            binding.row3.apply {
                setImageResource(R.drawable.horizontal)
            }
        } else if(WinPosition[0]==0 && WinPosition[1]==3 && WinPosition[2]==6){
            binding.col1.apply {
                setImageResource(R.drawable.veritcal)
            }
        }else if(WinPosition[0]==1 && WinPosition[1]==4 && WinPosition[2]==7){
            binding.row2.apply {
                setImageResource(R.drawable.veritcal)
            }
        }else if(WinPosition[0]==2 && WinPosition[1]==5 && WinPosition[2]==8){
            binding.col3.apply {
                setImageResource(R.drawable.veritcal)
            }
        }else if(WinPosition[0]==0 && WinPosition[1]==4 && WinPosition[2]==8){
            binding.row2.apply {
                setImageResource(R.drawable.maindiagonal)
            }
        }else if(WinPosition[0]==2 && WinPosition[1]==4 && WinPosition[2]==6){
            binding.row2.apply {
                setImageResource(R.drawable.offdiagonal)
            }
        }
    }

    private fun gameReset() {
        binding.startButton.text = "Click On Grid To play"
        gameActive = true
        activePlayer = 0
        counter = 0
        //set all position to Null
        Arrays.fill(gameState, 2)
        // remove all the images from the boxes inside the grid
        binding.imageView0.setImageResource(0)
        binding.imageView1.setImageResource(0)
        binding.imageView2.setImageResource(0)
        binding.imageView3.setImageResource(0)
        binding.imageView4.setImageResource(0)
        binding.imageView5.setImageResource(0)
        binding.imageView6.setImageResource(0)
        binding.imageView7.setImageResource(0)
        binding.imageView8.setImageResource(0)
        binding.row1.setImageResource(0)
        binding.row2.setImageResource(0)
        binding.row3.setImageResource(0)
        binding.col1.setImageResource(0)
        binding.col3.setImageResource(0)
        val status = findViewById<TextView>(com.example.myapplication.R.id.status)
//        status.text = "X's Turn - Tap to play"
    }
}