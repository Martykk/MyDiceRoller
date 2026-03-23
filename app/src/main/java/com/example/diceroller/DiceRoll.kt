package com.example.diceroller

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

// 這裡定義的 tableName 必須跟 DAO 裡的 SQL 語句一模一樣
@Entity(tableName = "dice_history")
data class DiceRoll(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val result: Int
)