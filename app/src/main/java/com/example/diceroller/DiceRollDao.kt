package com.example.diceroller

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiceRollDao {
    // 1. 取得所有紀錄，按時間降序排列（最新的在最上面）
    @Query("SELECT * FROM dice_history ORDER BY id DESC")
    fun getAllRolls(): Flow<List<DiceRoll>>

    // 2. 插入一筆新紀錄
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(diceRoll: DiceRoll)

    // 3. 清空所有紀錄（如果你想做一個「清除歷史」的功能）
    @Query("DELETE FROM dice_history")
    suspend fun clearAll()
}