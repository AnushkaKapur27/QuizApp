package com.quizapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.quizapp.data.local.dao.QuestionDao
import com.quizapp.data.local.entity.QuestionEntity

@Database(
    entities = [QuestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        const val DATABASE_NAME = "quiz_database"

        fun buildDatabase(context: Context): QuizDatabase {
            return Room.databaseBuilder(
                context,
                QuizDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}