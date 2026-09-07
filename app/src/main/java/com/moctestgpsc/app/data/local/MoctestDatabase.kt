package com.moctestgpsc.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moctestgpsc.app.data.local.dao.TestDao
import com.moctestgpsc.app.data.local.dao.QuestionDao
import com.moctestgpsc.app.data.local.dao.UserAnswerDao
import com.moctestgpsc.app.data.local.dao.TestResultDao
import com.moctestgpsc.app.data.local.entity.TestEntity
import com.moctestgpsc.app.data.local.entity.QuestionEntity
import com.moctestgpsc.app.data.local.entity.UserAnswerEntity
import com.moctestgpsc.app.data.local.entity.TestResultEntity

@Database(
    entities = [
        TestEntity::class,
        QuestionEntity::class,
        UserAnswerEntity::class,
        TestResultEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MoctestDatabase : RoomDatabase() {

    abstract fun testDao(): TestDao
    abstract fun questionDao(): QuestionDao
    abstract fun userAnswerDao(): UserAnswerDao
    abstract fun testResultDao(): TestResultDao

    companion object {
        @Volatile
        private var INSTANCE: MoctestDatabase? = null

        fun getDatabase(context: Context): MoctestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoctestDatabase::class.java,
                    "moctest_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
