package com.example.diceroller

import kotlinx.coroutines.flow.Flow

class DiceRepository(private val diceRollDao: DiceRollDao) {
    // 取得所有紀錄，並轉化為 Flow 供外部觀察
    val allRolls: Flow<List<DiceRoll>> = diceRollDao.getAllRolls()

    // 呼叫 DAO 的新增指令
    suspend fun insert(diceRoll: DiceRoll) {
        diceRollDao.insert(diceRoll)
    }

    suspend fun delete(diceRoll: DiceRoll){
        diceRollDao.delete(diceRoll)
    }
}