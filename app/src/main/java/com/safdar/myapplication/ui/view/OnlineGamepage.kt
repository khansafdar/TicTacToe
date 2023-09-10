package com.safdar.myapplication.ui.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.safdar.myapplication.R
import com.safdar.myapplication.databinding.OnlineGamePageBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.Arrays
import kotlin.system.exitProcess

class OnlineGamepage : AppCompatActivity() {
    val view = arrayListOf<View>()
    var isMyMove = MutableLiveData(isCodeMaker)
    private lateinit var binding: OnlineGamePageBinding
    var gameActive = true
    private var activePlayer = 0
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
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
        showCode()
        FirebaseDatabase.getInstance().reference.child("data").child(code)
            .addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var data = snapshot.value
                    if (isMyMove.value == true) {
                        isMyMove.value = false
                    } else {
                        isMyMove.value = true
                        moveOnLine(view[data.toString().toInt()])
                        checkWinner()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    gameReset()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun showCode() {
        if(isCodeMaker){
            binding.idGeneratedCode.text = code
        }
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

    private fun moveOnLine(view: View) {
        counter++
        val img = view as ImageView
        val tappedImage = img.tag.toString().toInt()
        gameState[tappedImage] = activePlayer
        activePlayer = if (activePlayer == 0) {
            img.setImageResource(R.drawable.x)
            1
        } else {
            img.setImageResource(R.drawable.o)
            0
        }
    }

    private fun observer() {
        isMyMove.observe(this) {
            if(gameActive) {
                if (isMyMove.value == true) {
                    binding.status.text = "Your turn"
                } else {
                    binding.status.text = "Other turn"
                }
            }
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun handleOnClickListener() {
        binding.backButton.setOnClickListener {
            removeCode()
            FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
            exitProcess(0)
        }
        binding.startButton.setOnClickListener {
            gameReset()
            FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()

        }
    }

    private fun DisbleClick() {
        binding.imageView0.isClickable = false
        binding.imageView1.isClickable = false
        binding.imageView2.isClickable = false
        binding.imageView3.isClickable = false
        binding.imageView4.isClickable = false
        binding.imageView5.isClickable = false
        binding.imageView6.isClickable = false
        binding.imageView7.isClickable = false
        binding.imageView8.isClickable = false
    }

    private fun EnableClick() {
        binding.imageView0.isClickable = true
        binding.imageView1.isClickable = true
        binding.imageView2.isClickable = true
        binding.imageView3.isClickable = true
        binding.imageView4.isClickable = true
        binding.imageView5.isClickable = true
        binding.imageView6.isClickable = true
        binding.imageView7.isClickable = true
        binding.imageView8.isClickable = true
    }

    fun playerTap(view: View) {
        if (isMyMove.value == true) {
            binding.startButton.text = "Reset Game"
            val img = view as ImageView
            val tappedImage = img.tag.toString().toInt()
            if (gameState[tappedImage] === 2) {
                counter++
                if (counter == 9) {
                    gameActive = false
                }
                gameState[tappedImage] = activePlayer
                img.translationY = -1000f
                if (activePlayer == 0) {
                    img.setImageResource(R.drawable.x)
                    activePlayer = 1
                } else {
                    img.setImageResource(R.drawable.o)
                    activePlayer = 0
                }
                img.animate().translationYBy(1000f).duration = 300
                updateDatabase(tappedImage)
            }
            checkWinner()
        }
    }

    private fun checkWinner() {
        var flag = 0
        if (counter > 4) {
            for (winPosition in winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] && gameState[winPosition[0]] != 2) {
                    flag = 1
                    drawLine(winPosition)
                    gameActive = false
                    val winnerStr: String = if (gameState[winPosition[0]] === 0) {
                        DisbleClick()
                        binding.startButton.text = "Reset Game"
                        "You has won"
                    } else {
                        DisbleClick()
                        binding.startButton.text = "Reset Game"
                        "Other has won"
                    }
                    binding.status.text = winnerStr
                }
            }
            if (counter == 9 && flag == 0) {
                DisbleClick()
                binding.startButton.text = "Reset Game"
                binding.status.text = "Match Draw"
            }
        }
    }

    private fun updateDatabase(cellId: Int) {
        FirebaseDatabase.getInstance().reference.child("data").child(code).push().setValue(cellId);
    }

    private fun drawLine(WinPosition: IntArray) {
        if (WinPosition[0] == 0 && WinPosition[1] == 1 && WinPosition[2] == 2) {
            binding.row1.apply {
                setImageResource(R.drawable.horizontal)
            }
        } else if (WinPosition[0] == 3 && WinPosition[1] == 4 && WinPosition[2] == 5) {
            binding.row2.apply {
                setImageResource(R.drawable.horizontal)
            }
        } else if (WinPosition[0] == 6 && WinPosition[1] == 7 && WinPosition[2] == 8) {
            binding.row3.apply {
                setImageResource(R.drawable.horizontal)
            }
        } else if (WinPosition[0] == 0 && WinPosition[1] == 3 && WinPosition[2] == 6) {
            binding.col1.apply {
                setImageResource(R.drawable.veritcal)
            }
        } else if (WinPosition[0] == 1 && WinPosition[1] == 4 && WinPosition[2] == 7) {
            binding.row2.apply {
                setImageResource(R.drawable.veritcal)
            }
        } else if (WinPosition[0] == 2 && WinPosition[1] == 5 && WinPosition[2] == 8) {
            binding.col3.apply {
                setImageResource(R.drawable.veritcal)
            }
        } else if (WinPosition[0] == 0 && WinPosition[1] == 4 && WinPosition[2] == 8) {
            binding.row2.apply {
                setImageResource(R.drawable.maindiagonal)
            }
        } else if (WinPosition[0] == 2 && WinPosition[1] == 4 && WinPosition[2] == 6) {
            binding.row2.apply {
                setImageResource(R.drawable.offdiagonal)
            }
        }
    }

    private fun gameReset() {
        isMyMove.value = isCodeMaker
        activePlayer = if (isCodeMaker) {0} else {1}
        binding.startButton.text = "Click On Grid To play"
        gameActive = true
        counter = 0
        Arrays.fill(gameState, 2)
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
        EnableClick()
    }
    private fun removeCode(){
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("codes").child(keyValue).removeValue()
        }
    }

    override fun onBackPressed() {
        removeCode()
        FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
        exitProcess(0)
    }
}