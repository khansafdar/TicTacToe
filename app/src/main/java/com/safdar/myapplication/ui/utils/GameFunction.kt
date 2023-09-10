package com.safdar.myapplication.ui.utils

import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class GameFunction:AppCompatActivity() {
    fun chooseRandomMove(board: ArrayList<String>, moves: ArrayList<Int>): Int {
        val possibleMoves = arrayListOf<Int>()

        for (i in moves) {
            if (board[i] == "") possibleMoves.add(i)
        }

        return if (possibleMoves.isEmpty()) {
            -1
        } else {
            val index = Random().nextInt(possibleMoves.count())
            possibleMoves[index]
        }
    }

    companion object {
        fun getRandomString(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }

}