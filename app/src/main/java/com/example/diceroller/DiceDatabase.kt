package com.example.diceroller

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 註冊有哪些 Entity (表格) 和版本號
@Database(entities = [DiceRoll::class], version = 1, exportSchema = false)
abstract class DiceDatabase : RoomDatabase() {

    // 2. 宣告抽象方法來取得 DAO
    abstract fun diceRollDao(): DiceRollDao

    companion object {
        @Volatile
        private var Instance: DiceDatabase? = null

        // 這是 MainActivity 會呼叫的方法
        fun getDatabase(context: Context): DiceDatabase {
            // 如果 Instance 不是空的就回傳；如果是空的就進入同步鎖定區建立它
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    DiceDatabase::class.java,
                    "dice_database" // 資料庫檔案的名稱
                )
                    // 如果版本更新但沒寫 Migration，就直接毀掉重建（開發階段好用）
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}