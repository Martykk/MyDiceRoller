package com.example.diceroller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiceViewModel (private val repository: DiceRepository): ViewModel() {
    // 骰子點數
    var result by mutableStateOf(1)
        private set

    // 總次數
    var rollCount by mutableStateOf(0)
        private set

    // 歷史紀錄
    // 將資料庫的 Flow 轉換成 StateFlow，讓 Compose 畫面能自動更新
    val rollHistory: StateFlow<List<DiceRoll>> = repository.allRolls
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 擲骰子動作
    fun rollDice() {
        val nextResult = (1..6).random()
        result = nextResult

        viewModelScope.launch {
            // 透過 repository 存入資料庫
            repository.insert(DiceRoll(result = nextResult))
        }
    }
}

class DiceViewModelFactory(private val repository: DiceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}