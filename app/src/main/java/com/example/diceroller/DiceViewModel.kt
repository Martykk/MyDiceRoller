package com.example.diceroller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DiceViewModel : ViewModel() {
    // 骰子點數
    var result by mutableStateOf(1)
        private set

    // 總次數
    var rollCount by mutableStateOf(0)
        private set

    // 歷史紀錄
    val rollHistory = mutableStateListOf<Int>()

    // 擲骰子動作
    fun rollDice() {
        val nextResult = (1..6).random()
        result = nextResult
        rollHistory.add(0, nextResult)
        rollCount++
    }
}